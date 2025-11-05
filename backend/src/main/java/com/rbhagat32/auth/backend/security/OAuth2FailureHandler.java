package com.rbhagat32.auth.backend.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;

@Component
public class OAuth2FailureHandler implements AuthenticationFailureHandler {

    @Value("${spring.profiles.active}")
    private String SPRING_PROFILES_ACTIVE;
    @Value("${frontend.url.dev}")
    private String FRONTEND_URL_DEV;
    @Value("${frontend.url.prod}")
    private String FRONTEND_URL_PROD;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        System.out.println("OAuth2 Error: " + exception);

        String frontendUrl = Objects.equals(SPRING_PROFILES_ACTIVE, "prod") ? FRONTEND_URL_PROD : FRONTEND_URL_DEV;
        response.sendRedirect(frontendUrl);
    }
}