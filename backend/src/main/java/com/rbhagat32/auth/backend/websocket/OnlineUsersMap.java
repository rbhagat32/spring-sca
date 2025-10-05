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

    public void addToOnlineUsersMap(String userId, String socketId) {
        onlineUsers.put(userId, socketId);
    }

    public void removeFromOnlineUsersMap(String socketId) {
        onlineUsers.entrySet().removeIf(entry -> entry.getValue().equals(socketId));
    }

    public List<UserDTO> getOnlineUsers() {
        Set<String> userIds = onlineUsers.keySet();
        List<UserEntity> userEntities = userRepository.findAllById(userIds);

        return userEntities.stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .toList();
    }
}