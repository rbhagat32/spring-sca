package com.rbhagat32.auth.backend.util;

import com.rbhagat32.auth.backend.dto.AuthResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {

    @Value("${cookie.expiration}")
    private long COOKIE_EXPIRATION;

    public ResponseCookie setCookie(AuthResponseDTO authResponseDTO, String cookieName) {
        return ResponseCookie
                .from(cookieName, authResponseDTO.getToken())
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/")
                .maxAge(COOKIE_EXPIRATION)
                .build();
    }

    public ResponseCookie removeCookie(String cookieName) {
        return ResponseCookie
                .from(cookieName, "")
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/").maxAge(0) // Expire the cookie immediately
                .build();
    }
}