package com.rbhagat32.auth.backend.controller;

import com.rbhagat32.auth.backend.entity.MessageEntity;
import com.rbhagat32.auth.backend.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/message")
@RequiredArgsConstructor
public class MessageController {

    private final MessageRepository messageRepository;

    @GetMapping("/get-all-messages")
    public List<MessageEntity> getAllMessages() {
        return messageRepository.findAll();
    }
}