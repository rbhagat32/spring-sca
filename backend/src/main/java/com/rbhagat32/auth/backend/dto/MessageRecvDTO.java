package com.rbhagat32.auth.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MessageRecvDTO {

    @NotBlank(message = "Message cannot be empty")
    @Size(min = 1, max = 100, message = "Message must be less than 100 characters")
    private String content;

    @NotBlank(message = "SenderId cannot be empty")
    private String senderId;
}