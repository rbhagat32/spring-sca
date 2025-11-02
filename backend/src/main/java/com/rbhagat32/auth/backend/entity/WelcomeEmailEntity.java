package com.rbhagat32.auth.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WelcomeEmailEntity {
    private String to;
    private String subject;
    private String body;
}
