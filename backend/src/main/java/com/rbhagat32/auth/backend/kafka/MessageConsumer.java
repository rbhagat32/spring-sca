package com.rbhagat32.auth.backend.kafka;

import com.rbhagat32.auth.backend.entity.MessageEntity;
import com.rbhagat32.auth.backend.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageConsumer {

    private final MessageRepository messageRepository;

    @KafkaListener(topics = "MESSAGES", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeMessage(MessageEntity message) {
        System.out.println("Message Consumed from Kafka: " + message);
        messageRepository.save(message);
    }
}