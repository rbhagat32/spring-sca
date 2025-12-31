package com.rbhagat32.auth.backend.redis;

import com.rbhagat32.auth.backend.websocket.OnlineUsersMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisSubscriber implements MessageListener {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final OnlineUsersMap onlineUsersMap;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String channel = new String(message.getChannel());
        String json = new String(message.getBody());

        log.info("Received Redis message from [{}]: {}", channel, json);

        if (channel.equals("MESSAGES")) {
            simpMessagingTemplate.convertAndSend("/topic/message", json);
        } else if (channel.equals("ONLINE_USERS") && json.equals("\"REFRESH\"")) { // JSON serializer adds \" before and after the String
            simpMessagingTemplate.convertAndSend("/topic/online-users", onlineUsersMap.getOnlineUsers());
            log.info("Broadcasted updated online users list to /topic/online-users");
        }
    }
}