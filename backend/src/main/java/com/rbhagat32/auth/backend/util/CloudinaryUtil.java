package com.rbhagat32.auth.backend.util;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.rbhagat32.auth.backend.dto.CloudinaryResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CloudinaryUtil {

    private final Cloudinary cloudinary;

    @SuppressWarnings("unchecked")
    public CloudinaryResponseDTO uploadFile(MultipartFile file) throws IOException {
        // Read original image
        BufferedImage originalImage = ImageIO.read(file.getInputStream());
        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();

        // Determine square crop area (center crop)
        int squareSize = Math.min(originalWidth, originalHeight);
        int x = (originalWidth - squareSize) / 2;
        int y = (originalHeight - squareSize) / 2;

        BufferedImage croppedImage = originalImage.getSubimage(x, y, squareSize, squareSize);

        // Detect format (preserve PNG transparency)
        String contentType = file.getContentType();
        boolean isPng = contentType != null && contentType.toLowerCase().contains("png");
        String format = isPng ? "png" : "jpg";
        int imageType = isPng ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB;

        // Resize cropped image to 400x400
        Image scaledImage = croppedImage.getScaledInstance(400, 400, Image.SCALE_SMOOTH);
        BufferedImage resizedImage = new BufferedImage(400, 400, imageType);

        Graphics2D g2d = resizedImage.createGraphics();
        g2d.drawImage(scaledImage, 0, 0, null);
        g2d.dispose();

        // Convert to byte array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(resizedImage, format, baos);
        byte[] resizedBytes = baos.toByteArray();

        // Upload to Cloudinary
        Map<String, Object> result = (Map<String, Object>) cloudinary.uploader().upload(
                resizedBytes,
                ObjectUtils.asMap("resource_type", "image")
        );

        return new CloudinaryResponseDTO(
                result.get("secure_url").toString(),
                result.get("public_id").toString(),
                result.get("resource_type").toString()
        );
    }

    public void deleteFile(String publicId) throws IOException {
        cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
    }
}