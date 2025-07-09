package com.daniel.app.airbnb.backend.mapper;


import com.daniel.app.airbnb.backend.dto.AuthenticatedUserResponseDto;
import com.daniel.app.airbnb.backend.dto.UpdateUserDTO;
import com.daniel.app.airbnb.backend.dto.UserDto;
import com.daniel.app.airbnb.backend.exception.NoSuchUserExp;
import com.daniel.app.airbnb.backend.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final PasswordEncoder passwordEncoder;
    private final BookingMapper bookingMapper;

    public UserDto toUserDto(User user) {
        if (user == null) {
            throw new NoSuchUserExp("user cannot be null");
        }
        return new UserDto(user.getName(), user.getEmail(),
                user.getPassword(), null, user.getJoinedYear());
    }

    public User updateUserFromDto(UpdateUserDTO dto, User user) {
        if (dto == null || user == null) {
            throw new IllegalArgumentException("DTO and User cannot be null");
        }
        if (dto.getName() != null) {
            user.setName(dto.getName());
        }
        if (dto.getEmail() != null) {
            user.setEmail(dto.getEmail());
        }
        if (dto.getPhone() != null) {
            user.setPhone(dto.getPhone());
        }
        if (dto.getJoinedYear() != null) {
            user.setJoinedYear(dto.getJoinedYear());
        }
        user.setSuperhost(dto.isSuperhost());
        if (dto.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        return user;
    }

    public AuthenticatedUserResponseDto toAuthenticatedUserDto(User user) {
        if (user == null) {
            throw new NoSuchUserExp("user cannot be null");
        }
        var bookings =
                user.getBookings().stream().map(bookingMapper::mapToBookingResponseDTO).toList();
        return new AuthenticatedUserResponseDto(
                user.getUserId(),
                user.isVerified(),
                user.getRegisterToken(),
                user.getProvider(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getRoles(),
                user.getJoinedYear(),
                user.isSuperhost(),
                user.getListings(),
                bookings

        );
    }


}
