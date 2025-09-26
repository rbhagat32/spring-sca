package com.rbhagat32.auth.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequestDTO {

    @NotBlank(message = "Name is mandatory")
    @Size(min = 3, max = 20, message = "Name size must be between 3 and 20")
    private String name;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    @Size(min = 3, max = 30, message = "Email size must be between 3 and 30")
    private String email;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 6, max = 30, message = "Password size must be between 6 and 30")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$", message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character")
    private String password;
}