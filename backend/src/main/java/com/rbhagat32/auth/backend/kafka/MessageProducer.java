package com.rbhagat32.auth.backend.kafka;

import com.rbhagat32.auth.backend.entity.MessageEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageProducer {

    private final KafkaTemplate<String, MessageEntity> kafkaTemplate;

    public void produceMessage(MessageEntity message) {
        kafkaTemplate.send("MESSAGES", message);
        log.info("Message Produced to Kafka: {}", message);
    }
}