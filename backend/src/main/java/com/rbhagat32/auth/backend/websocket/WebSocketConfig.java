package com.rbhagat32.auth.backend.websocket;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Value("${frontend.url.dev}")
    private String FRONTEND_URL_DEV;
    @Value("${frontend.url.prod}")
    private String FRONTEND_URL_PROD;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOrigins(FRONTEND_URL_DEV, FRONTEND_URL_PROD)
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Client sends events with destination starting with /emit
        config.setApplicationDestinationPrefixes("/emit");

        // Client subscribes here to receive messages
        config.enableSimpleBroker("/topic");
    }
}