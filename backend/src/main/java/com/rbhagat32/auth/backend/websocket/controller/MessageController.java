package com.rbhagat32.auth.backend.websocket.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class MessageController {

    @MessageMapping("/message")       // Client emits to:        /emit/message
    @SendTo("/topic/message")         // Server broadcasts to:   /topic/message
    public String sendMessage(String message) {
        System.out.println("📨 Incoming message: " + message);
        return message;
    }
}