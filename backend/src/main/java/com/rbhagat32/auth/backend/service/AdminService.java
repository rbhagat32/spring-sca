package com.rbhagat32.auth.backend.service;

import com.rbhagat32.auth.backend.dto.UserDTO;
import com.rbhagat32.auth.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream().map((user) -> modelMapper.map(user, UserDTO.class)).toList();
    }
}