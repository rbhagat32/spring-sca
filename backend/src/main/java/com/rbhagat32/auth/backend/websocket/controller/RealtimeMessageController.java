package com.rbhagat32.auth.backend.websocket.controller;

import com.rbhagat32.auth.backend.dto.MessageDTO;
import com.rbhagat32.auth.backend.dto.MessageRecvDTO;
import com.rbhagat32.auth.backend.dto.UserDTO;
import com.rbhagat32.auth.backend.entity.MessageEntity;
import com.rbhagat32.auth.backend.entity.UserEntity;
import com.rbhagat32.auth.backend.repository.MessageRepository;
import com.rbhagat32.auth.backend.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class RealtimeMessageController {

    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final ModelMapper modelMapper;

    @MessageMapping("/message")                                     // Client emits to:        /emit/message
    @SendTo("/topic/message")                                       // Server broadcasts to:   /topic/message
    public MessageDTO sendMessage(@Valid MessageRecvDTO message) {
        System.out.println("📨 Incoming message: " + message);

        UserEntity sender = userRepository.findById(message.getSenderId())
                .orElseThrow(() -> new UsernameNotFoundException("Sender not found"));

        MessageEntity newMessage = new MessageEntity();
        newMessage.setContent(message.getContent());
        newMessage.setSender(sender);

        MessageEntity savedMessage = messageRepository.save(newMessage);

        return new MessageDTO(
                savedMessage.getId(),
                savedMessage.getContent(),
                modelMapper.map(savedMessage.getSender(), UserDTO.class),
                savedMessage.getCreatedAt()
        );
    }
}