package com.daniel.app.airbnb.backend.filters;


import com.daniel.app.airbnb.backend.detail.CustomUserDetail;
import com.daniel.app.airbnb.backend.jwt.JwtService;
import com.daniel.app.airbnb.backend.model.User;
import com.daniel.app.airbnb.backend.util.ResponseUtil;
import com.daniel.app.airbnb.backend.util.RoleUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Service
public class JwtFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, IOException {
        log.info("In OncePerRequestFilter, processing request path: {}", request.getServletPath());
        // Skip filter for public endpoints
        if (isPublicEndpoint(request)) {
            filterChain.doFilter(request, response);
            return;
        }
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ") && SecurityContextHolder.getContext().getAuthentication() == null) {
            String token = extractTokenFromHeader(authHeader);
            try {
                String email = jwtService.getEmailClaim(token);
                CustomUserDetail customUserDetail = (CustomUserDetail) userDetailsService.loadUserByUsername(email);
                User user = customUserDetail.getUser();
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(
                                user,
                                null,
                                RoleUtil.authorities(user)
                        );
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                filterChain.doFilter(request, response);
                return;
            } catch (Exception exp) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                if (exp instanceof ExpiredJwtException) {
                    objectMapper.writeValue(response.getOutputStream(),
                            ResponseUtil.getResponse(null, null, "token expired",
                                    HttpStatus.UNAUTHORIZED));
                } else if (exp instanceof SignatureException) {
                    objectMapper.writeValue(response.getOutputStream(),
                            ResponseUtil.getResponse(null, null, "provide " +
                                            "proper signature of the token",
                                    HttpStatus.UNAUTHORIZED));
                } else {
                    objectMapper.writeValue(response.getOutputStream(),
                            ResponseUtil.getResponse(null, null, exp.getMessage(),
                                    HttpStatus.UNAUTHORIZED));
                }
                log.error("Something wrong with the token: {}", exp.getMessage());
                response.getOutputStream().flush();
                return;
            }
        } else {
            log.warn("Missing or invalid Authorization header");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            objectMapper.writeValue(response.getOutputStream(),
                    ResponseUtil.getResponse(null, null,
                            HttpStatus.UNAUTHORIZED.name(), HttpStatus.UNAUTHORIZED));
            response.getOutputStream().flush();
            return;
        }
    }


    private String extractTokenFromHeader(String authHeader) {
        return authHeader.substring("Bearer ".length());
    }

    private boolean isPublicEndpoint(HttpServletRequest request) {
        String path = request.getServletPath();
        String method = request.getMethod();

        return
                (path.equals("/api/v1/users/login") && method.equals("POST")) ||
                        (path.equals("/api/v1/users/register") && method.equals("POST")) ||
                        (path.equals("/api/v1/users/verify") && method.equals("GET")) ||
                        (path.equals("/api/v1/listings") && method.equals("GET")) ||
                        (path.equals("/api/v1/users/refresh/token") && method.equals("GET")) ||
                        (path.equals("/api/v1/contact") && method.equals("POST"));
    }

}