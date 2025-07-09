package com.daniel.app.airbnb.backend.service;


import com.daniel.app.airbnb.backend.dto.AuthenticatedUserResponseDto;
import com.daniel.app.airbnb.backend.dto.LoginDto;
import com.daniel.app.airbnb.backend.dto.UpdateUserDTO;
import com.daniel.app.airbnb.backend.dto.UserDto;
import com.daniel.app.airbnb.backend.exception.NoSuchUserExp;
import com.daniel.app.airbnb.backend.jwt.JwtService;
import com.daniel.app.airbnb.backend.mapper.BookingMapper;
import com.daniel.app.airbnb.backend.mapper.UserMapper;
import com.daniel.app.airbnb.backend.model.enums.Provider;
import com.daniel.app.airbnb.backend.model.User;
import com.daniel.app.airbnb.backend.repository.BookingRepository;
import com.daniel.app.airbnb.backend.repository.UserRepository;
import com.daniel.app.airbnb.backend.service.email.EmailService;
import com.daniel.app.airbnb.backend.util.MailUtil;
import com.daniel.app.airbnb.backend.util.TokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class UserService {


    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final JwtService jwtService;
    private final BookingRepository bookingRepository;
    @Value("${custom.created-account}")
    private String redirectUrl;
    @Value("${custom.mail.enabled:false}")
    private boolean emailEnabled;

    public List<UserDto> getUsers() {
        return userRepository.findAll().stream().map(userMapper::toUserDto).toList();
    }

    public UserDto getUser(Long userId) {
        var user = findUserById(userId);
        return Stream.of(user).map(userMapper::toUserDto).findFirst().get();

    }


    @Transactional
    public AuthenticatedUserResponseDto authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var user = (User) authentication.getPrincipal();
        var newUser = userRepository.findById(user.getUserId());
        newUser.get().getBookings();//avoiding proxy error
        return userMapper.toAuthenticatedUserDto(newUser.get());
    }

    public UserDto createUser(UserDto userDto) {
        var creatUser = new User(userDto.name(), userDto.email(), passwordEncoder.encode(userDto.password()), userDto.joinedYear(), Provider.LOCAL);

        String token = TokenUtil.confirmationToken();
        creatUser.setRegisterToken(token);
        creatUser.setVerified(false);
        var createdUser = userRepository.save(creatUser);
        if (emailEnabled) {
            emailService.sendEmail(MailUtil.getPayload(createdUser,
                    redirectUrl + token), "create-account");
        }
        return Stream.of(createdUser).map(userMapper::toUserDto).findFirst().get();
    }

    public User loginUser(LoginDto loginDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.email(), loginDto.password()));
            return userRepository.findUserByEmail(loginDto.email()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        } catch (BadCredentialsException ex) {
            throw new UsernameNotFoundException(ex.getMessage());
        }

    }


    public void deleteUser(Long userId) {
        if (userId == null) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User dbUser = (User) auth.getPrincipal();
            userRepository.deleteById(dbUser.getUserId());
        } else {
            userRepository.deleteById(userId);
        }

    }

    public User updateUser(UpdateUserDTO updateUserDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User dbUser = (User) auth.getPrincipal();
        User user = userMapper.updateUserFromDto(updateUserDTO, dbUser);
        return userRepository.save(user);
    }

    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email).orElseThrow(() -> new NoSuchUserExp("no such user"));
    }

    private User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new NoSuchUserExp("no such user"));
    }

    public void verifyUserByToken(String token) {
        Optional<User> userOptional = userRepository.findUserByRegisterToken(token);
        if (userOptional.isEmpty()) {
            throw new NoSuchUserExp("no user with such token");
        }
        User user = userOptional.get();
        user.setVerified(true);
        userRepository.save(user);
    }

    public Map<String, String> userTokens(String token) {
        String email = jwtService.getEmailClaim(token);
        User user = findUserByEmail(email);
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        return Map.of("access_token", accessToken, "access_refresh", refreshToken);
    }

}
