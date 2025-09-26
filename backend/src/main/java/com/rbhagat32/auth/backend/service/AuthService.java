package com.rbhagat32.auth.backend.service;

import com.rbhagat32.auth.backend.dto.*;
import com.rbhagat32.auth.backend.entity.UserEntity;
import com.rbhagat32.auth.backend.repository.UserRepository;
import com.rbhagat32.auth.backend.security.JwtUtil;
import com.rbhagat32.auth.backend.util.CloudinaryUtil;
import com.rbhagat32.auth.backend.util.ConversionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final ConversionUtil conversionUtil;
    private final CloudinaryUtil cloudinaryUtil;

    public AuthResponseDTO register(RegisterRequestDTO body, MultipartFile avatar) {
        if (userRepository.findByEmail(body.getEmail()).isPresent()) {
            throw new RuntimeException("Email is already registered");
        }

        Set<String> roles = new HashSet<>();
        roles.add("ROLE_USER");

        UserEntity user = new UserEntity();
        user.setName(body.getName());
        user.setEmail(body.getEmail());
        user.setPassword(passwordEncoder.encode(body.getPassword()));
        user.setRoles(roles);

        if (avatar != null && !avatar.isEmpty()) {
            try {
                CloudinaryResponseDTO uploadResponse = cloudinaryUtil.uploadFile(avatar);
                user.setAvatarId(uploadResponse.getPublicId());
                user.setAvatarUrl(uploadResponse.getUrl());
            } catch (Exception ex) {
                throw new RuntimeException("Failed to upload avatar" + ex);
            }
        }

        UserEntity savedUser = userRepository.save(user);
        String token = jwtUtil.generateToken(savedUser);
        return conversionUtil.convertToAuthResponseDTO(token, savedUser);
    }

    public AuthResponseDTO login(LoginRequestDTO body) {
        UserEntity user = userRepository.findByEmail(body.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email"));

        if (!passwordEncoder.matches(body.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtUtil.generateToken(user);
        return conversionUtil.convertToAuthResponseDTO(token, user);
    }

    public UserDTO getLoggedInUser1(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AuthenticationCredentialsNotFoundException("User is not authenticated");
        }

        UserEntity loggedInUser = (UserEntity) authentication.getPrincipal();
        return conversionUtil.convertToUserDTO(loggedInUser);
    }

    public UserDTO getLoggedInUser2(UserEntity user) {
        return conversionUtil.convertToUserDTO(user);
    }

    public UserDTO getLoggedInUser3() {
        UserEntity loggedInUser = ((UserEntity) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal());

        return conversionUtil.convertToUserDTO(loggedInUser);
    }
}