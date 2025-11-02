package com.rbhagat32.auth.backend.kafka;

import com.rbhagat32.auth.backend.entity.WelcomeEmailEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WelcomeEmailProducer {

    private final KafkaTemplate<String, WelcomeEmailEntity> kafkaTemplate;

    public void produceWelcomeEmail(String to) {
        WelcomeEmailEntity welcomeEmail = new WelcomeEmailEntity(
                to,
                "Welcome to Spring-SCA 🎉",
                "Hi there! Thanks for signing up. We’re excited to have you!"
        );

        kafkaTemplate.send("WELCOME_EMAILS", welcomeEmail);
        System.out.println("Welcome Email Produced to Kafka: " + welcomeEmail);
    }
}