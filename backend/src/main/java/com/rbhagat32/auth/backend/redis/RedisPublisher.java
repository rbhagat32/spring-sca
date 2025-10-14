package com.rbhagat32.auth.backend.redis;

import com.rbhagat32.auth.backend.entity.MessageEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisPublisher {

    private final RedisTemplate<String, Object> redisTemplate;

    public void publishMessage(String channel, MessageEntity message) {
        log.info("Publishing to Redis channel {}: {}", channel, message);
        redisTemplate.convertAndSend(channel, message);
    }

    public void refreshOnlineUsers() {
        log.info("Publishing Online Users to Redis");
        redisTemplate.convertAndSend("ONLINE_USERS", "REFRESH"); // REFRESH is a String, so serializer will add \" in start and end
    }
}