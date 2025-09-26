package com.rbhagat32.auth.backend.websocket;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class OnlineUsersMap {

    // Map<userId, sessionId>
    private final Map<String, String> onlineUsers = new ConcurrentHashMap<>();

    public void connect(String userId, String sessionId) {
        onlineUsers.put(userId, sessionId);
    }

    public void disconnect(String sessionId) {
        onlineUsers.entrySet().removeIf(entry -> entry.getValue().equals(sessionId));
    }

    public Map<String, String> getOnlineUsers() {
        return onlineUsers;
    }
}