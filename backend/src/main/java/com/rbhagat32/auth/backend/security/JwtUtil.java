package com.rbhagat32.auth.backend.security;

import com.rbhagat32.auth.backend.entity.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

@Slf4j
@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String JWT_SECRET;

    @Value("${jwt.expiration}")
    private long JWT_EXPIRATION;

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claimsResolver.apply(claims);
    }

    private boolean isTokenExpired(String token) {
        final Date expiration = extractClaim(token, Claims::getExpiration);
        return expiration.before(new Date());
    }

    public String generateToken(UserEntity user) {
        return Jwts.builder()
                .subject(user.getId())
//                .claim("email", user.getEmail())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION))
                .signWith(Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8)))
                .compact();
    }

    public String extractUserId(String token) {
        try {
            String userId = extractClaim(token, Claims::getSubject);
            return userId;
        } catch (JwtException | IllegalArgumentException ex) {
            throw new JwtException("Invalid or expired JWT token", ex);
        }
    }

    public boolean validateToken(String token, UserEntity user) {
        final String userIdFromToken = extractUserId(token);
        final String userIdFromDB = user.getId();

        return userIdFromToken != null &&
                !isTokenExpired(token) &&
                userIdFromToken.equals(userIdFromDB);
    }
}