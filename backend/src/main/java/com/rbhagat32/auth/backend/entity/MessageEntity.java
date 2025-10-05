package com.rbhagat32.auth.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "messages")
public class MessageEntity implements Serializable {

    @Id
    // @GeneratedValue(strategy = GenerationType.UUID) // set UUID manually inside controller
    private String id;

    private String content;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private UserEntity sender;

    // @CreationTimestamp // set createdAt manually inside controller
    @Column(updatable = false)
    private Instant createdAt;
}