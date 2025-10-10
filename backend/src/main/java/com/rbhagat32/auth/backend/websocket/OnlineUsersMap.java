package com.rbhagat32.auth.backend.websocket;

import com.rbhagat32.auth.backend.dto.UserDTO;
import com.rbhagat32.auth.backend.entity.UserEntity;
import com.rbhagat32.auth.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class OnlineUsersMap {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final RedisTemplate<String, String> redisTemplate;

    public void addToOnlineUsersMap(String userId, String socketId) {
        redisTemplate.opsForHash().put("online-users-map", userId, socketId);
    }

    public void removeFromOnlineUsersMap(String socketId) {
        HashOperations<String, String, String> ops = redisTemplate.opsForHash();
        Map<String, String> allEntries = ops.entries("online-users-map");

        allEntries.entrySet().stream()
                .filter(entry -> entry.getValue().equals(socketId))
                .findFirst()
                .ifPresent(entry -> ops.delete("online-users-map", entry.getKey()));
    }

    public List<UserDTO> getOnlineUsers() {
        HashOperations<String, String, String> ops = redisTemplate.opsForHash();
        Set<String> userIds = ops.keys("online-users-map");

        List<UserEntity> userEntities = userRepository.findAllById(userIds);
        return userEntities.stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .toList();
    }
}