package com.rbhagat32.auth.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class MessageDTO {

    private String id;
    private String content;
    private UserDTO sender;
    private Instant createdAt;
}