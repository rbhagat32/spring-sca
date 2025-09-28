package com.rbhagat32.auth.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "messages")
public class MessageEntity {

    @Id
    // @GeneratedValue(strategy = GenerationType.UUID) // this has to be disabled to set UUID manually inside controller
    private String id;

    private String content;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private UserEntity sender;

    @CreationTimestamp
    @Column(updatable = false)
    private Instant createdAt;
}