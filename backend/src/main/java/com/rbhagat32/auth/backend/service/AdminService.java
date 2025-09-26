package com.rbhagat32.auth.backend.service;

import com.rbhagat32.auth.backend.dto.UserDTO;
import com.rbhagat32.auth.backend.repository.UserRepository;
import com.rbhagat32.auth.backend.util.ConversionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final ConversionUtil conversionUtil;

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream().map(conversionUtil::convertToUserDTO).toList();
    }
}