package com.rbhagat32.auth.backend.dto;

import lombok.Data;

@Data
public class AuthResponseDTO {

    private String token;
    private UserDTO userDTO;
}
