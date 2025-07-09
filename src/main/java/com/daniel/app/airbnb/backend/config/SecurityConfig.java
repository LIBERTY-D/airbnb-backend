package com.daniel.app.airbnb.backend.config;


import com.daniel.app.airbnb.backend.OAuth2.OAuth2LoginSuccessHandler;
import com.daniel.app.airbnb.backend.detail.CustomUserDetailService;
import com.daniel.app.airbnb.backend.environment.OriginsEnv;
import com.daniel.app.airbnb.backend.filters.JwtFilter;
import com.daniel.app.airbnb.backend.res.HttpResponse;
import com.daniel.app.airbnb.backend.service.CustomOAuth2UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.time.LocalDateTime;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomUserDetailService customUserDetailService;
    private final JwtFilter jwtFilter;
    private final OriginsEnv originsEnv;
    private final ObjectMapper objectMapper;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> {
                    configurePublicEndpoints(auth);
                    configureAdminEndpoints(auth);
                    configureUserEndpoints(auth);
                    configureBookingEndpoints(auth);

                    auth.anyRequest().authenticated();
                })
                .anonymous(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .oauth2Login(oauth2 -> oauth2
                        .successHandler(oAuth2LoginSuccessHandler)
                        .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(handler -> handler.accessDeniedHandler(accessDeniedHandler()))
                .build();
    }

    private void configurePublicEndpoints(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth) {
        auth.requestMatchers(
                "/api/v1/users/login",
                "/api/v1/users/register",
                "/api/v1/users/verify",
                "/api/v1/users/refresh/token",
                "/api/v1/contact",
                "/api/v1/listings" // general listing access
        ).permitAll();
    }

    private void configureAdminEndpoints(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth) {
        auth.requestMatchers(HttpMethod.DELETE, "/api/v1/listings/**").hasRole("ADMIN");
        auth.requestMatchers(HttpMethod.DELETE, "/api/v1/users/**").hasAnyRole("USER", "HOST", "ADMIN");
    }

    private void configureUserEndpoints(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth) {
        auth.requestMatchers(HttpMethod.PATCH, "/api/v1/users").hasAnyRole("ADMIN", "HOST", "USER");

        auth.requestMatchers(HttpMethod.GET, "/api/v1/listings/**").hasAnyRole("USER", "HOST", "ADMIN");
        auth.requestMatchers(HttpMethod.GET, "/api/v1/users/**").hasAnyRole("USER", "ADMIN");

        auth.requestMatchers(HttpMethod.POST, "/api/v1/listings").hasAnyRole("HOST", "ADMIN");
        auth.requestMatchers(HttpMethod.PATCH, "/api/v1/listings/**").hasAnyRole("HOST", "ADMIN");
    }

    private void configureBookingEndpoints(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth) {
        auth.requestMatchers(HttpMethod.POST, "/api/v1/bookings/**").hasAnyRole("USER", "ADMIN");
        auth.requestMatchers(HttpMethod.GET, "/api/v1/bookings").hasRole("ADMIN");
        auth.requestMatchers(HttpMethod.GET, "/api/v1/bookings/**").hasAnyRole("USER", "ADMIN");
        auth.requestMatchers(HttpMethod.PATCH, "/api/v1/bookings/**").hasAnyRole("USER", "ADMIN");
        auth.requestMatchers(HttpMethod.DELETE, "/api/v1/bookings/**").hasRole("ADMIN");
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin(originsEnv.getOrigins());
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.setAllowCredentials(false);//using token so false
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    AccessDeniedHandler accessDeniedHandler() {
        return (HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) -> {
            HttpResponse<Object> httpResponse = HttpResponse.builder()
                    .status(HttpStatus.FORBIDDEN)
                    .statusCode(HttpStatus.FORBIDDEN.value())
                    .message("You don't have privileges for this route")
                    .timeStamp(LocalDateTime.now())
                    .build();
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            objectMapper.writeValue(response.getOutputStream(), httpResponse);
            response.getOutputStream().flush();
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {

        return authConfig.getAuthenticationManager();


    }
}
