package com.rbhagat32.auth.backend.kafka;

import com.rbhagat32.auth.backend.entity.UserEntity;
import com.rbhagat32.auth.backend.entity.WelcomeEmailEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class WelcomeEmailProducer {

    private final KafkaTemplate<String, WelcomeEmailEntity> kafkaTemplate;

    public void produceWelcomeEmail(UserEntity user) {
        String subject = "Welcome to Spring-SCA !";

        String body = String.format("""
                    <div style="font-family: 'Segoe UI', Arial, sans-serif; background-color: #f9fafb; padding: 24px; border-radius: 10px; color: #333; line-height: 1.6;">
                        <h2 style="color: #1a202c;">Welcome to Spring-SCA !</h2>
                        <p>Hi %s,</p>
                
                        <p>We’re thrilled to have you join our growing community !</p>
                        <p>Here’s what you can do next:</p>
                
                        <ul style="padding-left: 20px;">
                            <li>📘 Check out our quick-start guide to learn the basics.</li>
                            <li>✨ Explore your dashboard and personalize your profile.</li>
                            <li>💬 Connect and collaborate with other developers.</li>
                        </ul>
                
                        <p>If you ever need help, our support team is just a click away.</p>
                
                        <p style="margin-top: 24px;">Thanks again for signing up — we can’t wait to see what you build with <strong>Spring-SCA</strong> !</p>
                
                        <p style="margin-top: 20px;">Regards,<br><strong>Spring-SCA Team</strong></p>
                    </div>
                """, user.getName() != null ? user.getName() : "there");

        WelcomeEmailEntity welcomeEmail = new WelcomeEmailEntity(
                user.getEmail(),
                subject,
                body
        );

        kafkaTemplate.send("WELCOME_EMAILS", welcomeEmail);
        log.info("Welcome Email Produced to Kafka: {}", welcomeEmail);
    }
}