package com.rbhagat32.auth.backend.websocket;

import com.rbhagat32.auth.backend.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final OnlineUsersMap onlineUsersMap;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @EventListener
    public void handleWebSocketConnect(SessionConnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = accessor.getSessionId();

        Authentication auth = (Authentication) accessor.getUser();
        if (auth != null && auth.getPrincipal() instanceof UserEntity user) {
            String databaseId = user.getId();

            onlineUsersMap.connect(databaseId, sessionId);
            simpMessagingTemplate.convertAndSend("/topic/online-users", onlineUsersMap.getOnlineUsers());

            System.out.println("✅ User connected: " + databaseId + " -> " + sessionId);
        }
    }

    @EventListener
    public void handleWebSocketDisconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = accessor.getSessionId();

        onlineUsersMap.disconnect(sessionId);
        simpMessagingTemplate.convertAndSend("/topic/online-users", onlineUsersMap.getOnlineUsers());

        System.out.println("❌ User disconnected: " + sessionId);
    }
}