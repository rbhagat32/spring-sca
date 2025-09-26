package com.rbhagat32.auth.backend.util;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.rbhagat32.auth.backend.dto.CloudinaryResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CloudinaryUtil {

    private final Cloudinary cloudinary;

    @SuppressWarnings("unchecked")
    public CloudinaryResponseDTO uploadFile(MultipartFile file) throws IOException {
        Map<String, Object> result = (Map<String, Object>) cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap("resource_type", "auto"));

        return new CloudinaryResponseDTO(
                result.get("secure_url").toString(),
                result.get("public_id").toString(),
                result.get("resource_type").toString());
    }

    public void deleteFile(String publicId) throws IOException {
        cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
    }
}