package com.rbhagat32.auth.backend.controller;

import com.rbhagat32.auth.backend.dto.UserDTO;
import com.rbhagat32.auth.backend.websocket.OnlineUsersMap;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final OnlineUsersMap onlineUsersMap;

    @GetMapping("/get-online-users")
    public ResponseEntity<List<UserDTO>> getOnlineUserIds() {
        return ResponseEntity.ok(onlineUsersMap.getOnlineUsers());
    }
}