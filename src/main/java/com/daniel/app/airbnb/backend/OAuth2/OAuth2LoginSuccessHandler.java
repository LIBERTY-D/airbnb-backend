package com.daniel.app.airbnb.backend.OAuth2;

import com.daniel.app.airbnb.backend.jwt.JwtService;
import com.daniel.app.airbnb.backend.model.User;
import com.daniel.app.airbnb.backend.model.enums.Provider;
import com.daniel.app.airbnb.backend.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Year;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {


    private final JwtService jwtService;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;
    @Value("${custom.redirect-url}")
    private String redirectFrontendUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
        String email = oauthUser.getAttribute("email");
        String name = oauthUser.getAttribute("name");
        Provider provider = determineProvider(oauthUser);
        Optional<User> user = userRepository.findUserByEmail(email);
        if (user.isEmpty()) {
            String dummyPassword = UUID.randomUUID().toString(); //random
            // since password would be managed by the provider
            String encodedPassword = new BCryptPasswordEncoder().encode(dummyPassword);
            int currentYear = Year.now().getValue();
            var createdUser = new User(name, email, encodedPassword, currentYear, provider);
            createdUser.setVerified(true);
            userRepository.save(createdUser);
        }
        var accessToken = jwtService.generateAccessToken(email);
        var refreshToken = jwtService.generateRefreshToken(email);
        //        var data = Map.of("access_token", accessToken, "refresh_token", refreshToken);
        //        objectMapper.writeValue(  response.getOutputStream(),data);
        //        response.getOutputStream().flush();
        //         Redirect with token as URL parameter (or return it as JSON if preferred)
        String redirectUrl =
                redirectFrontendUrl + "?access_token=" + accessToken +
                        "&refresh_token=" + refreshToken;
        response.sendRedirect(redirectUrl);
    }

    private Provider determineProvider(OAuth2User user) {
        if (user.getAttribute("sub") != null) return Provider.GOOGLE;
        if (user.getAttribute("login") != null) return Provider.GITHUB;
        return Provider.LOCAL;
    }

}
