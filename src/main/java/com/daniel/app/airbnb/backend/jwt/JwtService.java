package com.daniel.app.airbnb.backend.jwt;


import com.daniel.app.airbnb.backend.environment.JwtEnv;
import com.daniel.app.airbnb.backend.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final JwtEnv jwtEnv;

    public String generateAccessToken(User user) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        return Jwts.builder().
                signWith(key()).
                subject(user.getUserId().toString()).
                claims(addClaims(user)).
                issuedAt(now).
                expiration(new Date(nowMillis + jwtEnv.getJwtAccessExp())).
                compact();
    }

    public String generateAccessToken(String email) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        return Jwts.builder().
                signWith(key()).
                subject(email).
                claims(addClaims(email)).
                issuedAt(now).
                expiration(new Date(nowMillis + jwtEnv.getJwtAccessExp())).
                compact();
    }

    public <R> R extractClaims(String token, Function<Claims, R> resolver) {
        Claims claims = decodeToken(token);
        return resolver.apply(claims);
    }

    public Long getIdClaim(String token) {
        return Long.valueOf(extractClaims(token, Claims::getSubject));
    }

    public String getEmailClaim(String token) {
        return extractClaims(token, claims -> claims.get("email")).toString();
    }

    public Claims decodeToken(String token) {
        return Jwts
                .parser()
                .verifyWith(key())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String generateRefreshToken(User user) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        return Jwts.builder().
                signWith(key()).
                subject(user.getUserId().toString()).
                claims(addClaims(user)).
                issuedAt(now).
                expiration(new Date(nowMillis + jwtEnv.getJwtRefreshExp())).
                compact();

    }

    public String generateRefreshToken(String email) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        return Jwts.builder().
                signWith(key()).
                subject(email).
                claims(addClaims(email)).
                issuedAt(now).
                expiration(new Date(nowMillis + jwtEnv.getJwtRefreshExp())).
                compact();

    }


    private Map<String, ?> addClaims(User user) {
        return Map.of("email", user.getEmail());
    }

    private Map<String, ?> addClaims(String email) {
        return Map.of("email", email);
    }


    private SecretKey key() {
        String secret = jwtEnv.getJwtSecret();
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
