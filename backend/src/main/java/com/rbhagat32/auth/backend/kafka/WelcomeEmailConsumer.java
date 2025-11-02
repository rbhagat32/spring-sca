package com.rbhagat32.auth.backend.kafka;

import com.rbhagat32.auth.backend.entity.WelcomeEmailEntity;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WelcomeEmailConsumer {

    private final JavaMailSender javaMailSender;

    @KafkaListener(topics = "WELCOME_EMAILS", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeWelcomeEmail(WelcomeEmailEntity welcomeEmail) {
        System.out.println("Welcome Email Consumed from Kafka: " + welcomeEmail);

        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");

            helper.setTo(welcomeEmail.getTo());
            helper.setSubject(welcomeEmail.getSubject());
            helper.setText(welcomeEmail.getBody(), true);

            javaMailSender.send(message);
            System.out.println("Welcome Email sent to: " + welcomeEmail.getTo());

        } catch (MessagingException e) {
            System.err.println("Failed to send welcome email to: " + welcomeEmail.getTo());
        }
    }
}