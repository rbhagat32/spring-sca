package com.rbhagat32.auth.backend.websocket.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class MessageController {

    @MessageMapping("/send-message") // Client sends to: /app/sendMessage
    @SendTo("/topic/notifications") // Broadcasts to:   /topic/notifications
    public String sendMessage(String message) {
        System.out.println("📨 Incoming message: " + message);
        return message;
    }
}