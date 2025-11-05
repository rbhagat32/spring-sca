package com.rbhagat32.auth.backend.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rbhagat32.auth.backend.dto.AuthResponseDTO;
import com.rbhagat32.auth.backend.service.AuthService;
import com.rbhagat32.auth.backend.util.CookieUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    @Value("${spring.profiles.active}")
    private String SPRING_PROFILES_ACTIVE;
    @Value("${frontend.url.dev}")
    private String FRONTEND_URL_DEV;
    @Value("${frontend.url.prod}")
    private String FRONTEND_URL_PROD;

    private final ApplicationContext context;
    private final ObjectMapper objectMapper;
    private final CookieUtil cookieUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        OAuth2User user = token.getPrincipal();

        String registrationId = token.getAuthorizedClientRegistrationId();

        AuthService authService = context.getBean(AuthService.class);
        AuthResponseDTO authResponse = authService.OAuth2Login(user, registrationId);

        ResponseCookie cookie = cookieUtil.setCookie(authResponse);

        String frontendUrl = Objects.equals(SPRING_PROFILES_ACTIVE, "prod") ? FRONTEND_URL_PROD : FRONTEND_URL_DEV;

        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        response.sendRedirect(frontendUrl);
    }
}