package com.rbhagat32.auth.backend.service;

import com.rbhagat32.auth.backend.dto.*;
import com.rbhagat32.auth.backend.entity.UserEntity;
import com.rbhagat32.auth.backend.enums.RoleEnum;
import com.rbhagat32.auth.backend.kafka.WelcomeEmailProducer;
import com.rbhagat32.auth.backend.repository.UserRepository;
import com.rbhagat32.auth.backend.security.JwtUtil;
import com.rbhagat32.auth.backend.util.CloudinaryUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
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
    private final CloudinaryUtil cloudinaryUtil;
    private final ModelMapper modelMapper;
    private final WelcomeEmailProducer producer;

    public AuthResponseDTO register(RegisterRequestDTO body, MultipartFile avatar) {
        if (userRepository.findByEmail(body.getEmail()).isPresent()) {
            throw new BadCredentialsException("Email is already registered");
        }

        Set<RoleEnum> roles = new HashSet<>();
        roles.add(RoleEnum.ROLE_USER);

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

        producer.produceWelcomeEmail(savedUser);

        return new AuthResponseDTO(token, modelMapper.map(savedUser, UserDTO.class));
    }

    public AuthResponseDTO login(LoginRequestDTO body) {
        UserEntity user = userRepository.findByEmail(body.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid email"));

        if (!passwordEncoder.matches(body.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        String token = jwtUtil.generateToken(user);
        return new AuthResponseDTO(token, modelMapper.map(user, UserDTO.class));
    }

    public UserDTO getLoggedInUser1(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AuthenticationCredentialsNotFoundException("User is not authenticated");
        }

        UserEntity loggedInUser = (UserEntity) authentication.getPrincipal();
        return modelMapper.map(loggedInUser, UserDTO.class);
    }

    public UserDTO getLoggedInUser2(UserEntity user) {
        return modelMapper.map(user, UserDTO.class);
    }

    public UserDTO getLoggedInUser3() {
        UserEntity loggedInUser = ((UserEntity) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal());

        return modelMapper.map(loggedInUser, UserDTO.class);
    }
}