package com.rbhagat32.auth.backend.security;

import com.rbhagat32.auth.backend.entity.UserEntity;
import com.rbhagat32.auth.backend.repository.UserRepository;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            // log.info("JWT Filter: Incoming request on {}", request.getRequestURI());

            String token = null;
            String userId = null;

            // try getting JWT token form Authorization header
            final String AuthHeader = request.getHeader("Authorization");
            if (AuthHeader != null && AuthHeader.startsWith("Bearer ")) {
                token = AuthHeader.split("Bearer ")[1];
            }

            // try getting JWT token form Cookies
            if (token == null) {
                if (request.getCookies() != null) {
                    Cookie[] cookies = request.getCookies();
                    for (Cookie cookie : cookies) {
                        if (cookie.getName().equals("TOKEN")) {
                            token = cookie.getValue();
                            break;
                        }
                    }
                }
            }

            // if JWT token not found from Headers and Cookies both
            if (token == null) {
                log.info("No token found !");
                filterChain.doFilter(request, response);
                return;
            }

            // if JWT token found from somewhere, extract userId from subject
            // exceptions have been handled inside extractUserId function
            userId = jwtUtil.extractUserId(token);

            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                UserEntity userFromDB = userRepository.findById(userId)
                        .orElseThrow(() -> new JwtException("User not found from extracted userId !"));

                if (!jwtUtil.validateToken(token, userFromDB)) {
                    log.error("Invalid token !");
                    filterChain.doFilter(request, response);
                    return;
                }

                List<SimpleGrantedAuthority> authorities = userFromDB
                        .getRoles()
                        .stream()
                        .map(role -> new SimpleGrantedAuthority(role.name()))
                        .toList();

                UsernamePasswordAuthenticationToken loggedInUser = new UsernamePasswordAuthenticationToken(
                        userFromDB,
                        null,
                        authorities);

                loggedInUser.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(loggedInUser);
            }

            filterChain.doFilter(request, response);
        } catch (Exception ex) {
            handlerExceptionResolver.resolveException(request, response, null, ex);
        }
    }
}