package com.rbhagat32.auth.backend.util;

import com.rbhagat32.auth.backend.dto.AuthResponseDTO;
import com.rbhagat32.auth.backend.dto.UserDTO;
import com.rbhagat32.auth.backend.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class ConversionUtil {

    public AuthResponseDTO convertToAuthResponseDTO(String token, UserEntity user) {
        AuthResponseDTO authResponseDTO = new AuthResponseDTO();
        authResponseDTO.setToken(token);
        authResponseDTO.setUserDTO(convertToUserDTO(user));
        return authResponseDTO;
    }

    public UserDTO convertToUserDTO(UserEntity user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setAvatarId(user.getAvatarId());
        userDTO.setAvatarUrl(user.getAvatarUrl());
        userDTO.setRoles(user.getRoles());
        userDTO.setCreatedAt(user.getCreatedAt());
        userDTO.setUpdatedAt(user.getUpdatedAt());
        return userDTO;
    }
}