package com.rbhagat32.auth.backend.controller;

import com.rbhagat32.auth.backend.dto.AuthResponseDTO;
import com.rbhagat32.auth.backend.dto.LoginRequestDTO;
import com.rbhagat32.auth.backend.dto.RegisterRequestDTO;
import com.rbhagat32.auth.backend.dto.UserDTO;
import com.rbhagat32.auth.backend.entity.UserEntity;
import com.rbhagat32.auth.backend.service.AuthService;
import com.rbhagat32.auth.backend.util.CookieUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final CookieUtil cookieUtil;

    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AuthResponseDTO> register(
            @Valid @ModelAttribute RegisterRequestDTO body,
            @RequestPart(value = "avatar", required = false) MultipartFile avatar) {

        AuthResponseDTO registerResponse = authService.register(body, avatar);
        ResponseCookie cookie = cookieUtil.setCookie(registerResponse, "TOKEN");

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(registerResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO body) {
        AuthResponseDTO loginResponse = authService.login(body);
        ResponseCookie cookie = cookieUtil.setCookie(loginResponse, "TOKEN");

        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(loginResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("You are already Logged Out !");
        }

        ResponseCookie cookie = cookieUtil.removeCookie("TOKEN");

        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("Logged Out Successfully !");
    }

    @GetMapping("/get-user-1")
    public ResponseEntity<UserDTO> getLoggedInUser1(Authentication authentication) {
        return ResponseEntity.ok(authService.getLoggedInUser1(authentication));
    }

    @GetMapping("/get-user-2")
    public ResponseEntity<UserDTO> getLoggedInUser2(@AuthenticationPrincipal UserEntity user) {
        return ResponseEntity.ok(authService.getLoggedInUser2(user));
    }

    @GetMapping("/get-user-3")
    public ResponseEntity<UserDTO> getLoggedInUser3() {
        return ResponseEntity.ok(authService.getLoggedInUser3());
    }
}