package com.rbhagat32.auth.backend.kafka;

import com.rbhagat32.auth.backend.entity.MessageEntity;
import com.rbhagat32.auth.backend.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageConsumer {

    private final MessageRepository messageRepository;

    @KafkaListener(topics = "MESSAGES", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeMessage(MessageEntity message) {
        log.info("Message Consumed from Kafka: {}", message);
        messageRepository.save(message);
    }
}