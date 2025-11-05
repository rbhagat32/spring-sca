package com.rbhagat32.auth.backend.security;

import com.rbhagat32.auth.backend.entity.UserEntity;
import com.rbhagat32.auth.backend.enums.OAuth2ProviderEnum;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.core.user.OAuth2User;
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

    public OAuth2ProviderEnum getProviderTypeFromRegistrationId(String registrationId) {
        return switch (registrationId.toLowerCase()) {
            case "google" -> OAuth2ProviderEnum.GOOGLE;
            case "github" -> OAuth2ProviderEnum.GITHUB;
            default -> throw new IllegalArgumentException("Unsupported OAuth2 Provider: " + registrationId);
        };
    }

    public String getProviderIdFromOAuth2User(OAuth2User user, String registrationId) {
        String providerId = switch (registrationId.toLowerCase()) {
            case "google" -> user.getAttribute("sub");
            case "github" -> user.getAttribute("id").toString();

            default -> {
                log.error("Unsupported OAuth2 provider: {}", registrationId);
                throw new IllegalArgumentException("Unsupported OAuth2 Provider: " + registrationId);
            }
        };

        if (providerId == null || providerId.isBlank()) {
            log.error("Unable to determine providerId for Provider: {}", registrationId);
            throw new IllegalArgumentException("Unable to determine providerId for OAuth2 login");
        }

        return providerId;
    }

    public String getAvatarUrl(OAuth2User user, String registrationId) {
        return switch (registrationId.toLowerCase()) {
            case "google" -> user.getAttribute("picture");
            case "github" -> user.getAttribute("avatar_url");
            default -> null;
        };
    }
}