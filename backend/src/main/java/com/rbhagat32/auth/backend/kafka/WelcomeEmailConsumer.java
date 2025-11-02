package com.rbhagat32.auth.backend.kafka;

import com.rbhagat32.auth.backend.entity.WelcomeEmailEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WelcomeEmailConsumer {

    private final JavaMailSender javaMailSender;

    @KafkaListener(topics = "WELCOME_EMAILS", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeWelcomeEmail(WelcomeEmailEntity welcomeEmail) {
        System.out.println("Welcome Email Consumed from Kafka: " + welcomeEmail);

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(welcomeEmail.getTo());
        email.setSubject(welcomeEmail.getSubject());
        email.setText(welcomeEmail.getBody());
        javaMailSender.send(email);

        System.out.println("Welcome Email sent to: " + welcomeEmail.getTo());
    }
}