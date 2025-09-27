package com.rbhagat32.auth.backend.websocket;

import com.rbhagat32.auth.backend.dto.UserDTO;
import com.rbhagat32.auth.backend.entity.UserEntity;
import com.rbhagat32.auth.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class OnlineUsersMap {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    // Map<userId, sessionId>
    private final Map<String, String> onlineUsers = new ConcurrentHashMap<>();

    public void connect(String userId, String sessionId) {
        onlineUsers.put(userId, sessionId);
    }

    public void disconnect(String sessionId) {
        onlineUsers.entrySet().removeIf(entry -> entry.getValue().equals(sessionId));
    }

    public List<UserDTO> getOnlineUsers() {
        Set<String> userIds = onlineUsers.keySet();
        List<UserEntity> userEntities = userRepository.findAllById(userIds);

        return userEntities.stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .toList();
    }
}