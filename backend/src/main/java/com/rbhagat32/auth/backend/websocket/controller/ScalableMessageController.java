package com.rbhagat32.auth.backend.websocket.controller;

import com.rbhagat32.auth.backend.dto.MessageRecvDTO;
import com.rbhagat32.auth.backend.entity.MessageEntity;
import com.rbhagat32.auth.backend.entity.UserEntity;
import com.rbhagat32.auth.backend.kafka.KafkaProducer;
import com.rbhagat32.auth.backend.redis.RedisPublisher;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import java.time.Instant;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ScalableMessageController {

    private final RedisPublisher pub;
    private final KafkaProducer producer;

    @MessageMapping("/message-scalable")
    public void sendMessage(@Valid MessageRecvDTO message, SimpMessageHeaderAccessor headerAccessor) {
        log.info("Server: Scalable Realtime message received: {}", message);

        UserEntity sender = (UserEntity) ((Authentication) headerAccessor.getUser()).getPrincipal();

        MessageEntity newMessage = new MessageEntity(
                UUID.randomUUID().toString(),
                message.getContent(),
                sender,
                Instant.now()
        );

        pub.publishMessage("MESSAGES", newMessage);
        producer.produceMessage(newMessage);
    }
}