package com.rbhagat32.auth.backend.websocket;

import com.rbhagat32.auth.backend.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {

    private final OnlineUsersMap onlineUsersMap;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @EventListener
    public void handleWebSocketConnect(SessionConnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String socketId = accessor.getSessionId();

        Authentication auth = (Authentication) accessor.getUser();
        if (auth != null && auth.getPrincipal() instanceof UserEntity user) {
            String userId = user.getId();

            onlineUsersMap.addToOnlineUsersMap(userId, socketId);
            simpMessagingTemplate.convertAndSend("/topic/online-users", onlineUsersMap.getOnlineUsers());

            log.info("✅ User connected: {} -> {}", userId, socketId);
        }
    }

    @EventListener
    public void handleWebSocketDisconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = accessor.getSessionId();

        onlineUsersMap.removeFromOnlineUsersMap(sessionId);
        simpMessagingTemplate.convertAndSend("/topic/online-users", onlineUsersMap.getOnlineUsers());

        log.info("❌ User disconnected: {}", sessionId);
    }
}