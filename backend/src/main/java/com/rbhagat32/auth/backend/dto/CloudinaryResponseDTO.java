package com.rbhagat32.auth.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CloudinaryResponseDTO {

    private String url;
    private String publicId;
    private String resourceType;
}