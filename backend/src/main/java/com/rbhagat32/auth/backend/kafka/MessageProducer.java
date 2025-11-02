package com.rbhagat32.auth.backend.kafka;

import com.rbhagat32.auth.backend.entity.MessageEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageProducer {

    private final KafkaTemplate<String, MessageEntity> kafkaTemplate;

    public void produceMessage(MessageEntity message) {
        kafkaTemplate.send("MESSAGES", message);
        System.out.println("Message Produced to Kafka: " + message);
    }
}