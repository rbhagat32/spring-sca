package com.rbhagat32.auth.backend.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic messageTopic() {
        return new NewTopic("MESSAGES", 1, (short) 1);
    }

    @Bean
    public NewTopic welcomeEmailTopic() {
        return new NewTopic("WELCOME_EMAILS", 1, (short) 1);
    }
}