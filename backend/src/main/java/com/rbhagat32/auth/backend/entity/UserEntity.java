package com.rbhagat32.auth.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rbhagat32.auth.backend.enums.OAuth2ProviderEnum;
import com.rbhagat32.auth.backend.enums.RoleEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.Collection;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "users")
@Table(indexes = {
        @Index(name = "provider", columnList = "providerId, providerType")
})
public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;

    @Column(nullable = false)
    private String email;

    private String password;

    private String avatarId;
    private String avatarUrl;

    private String providerId;
    @Enumerated(EnumType.STRING)
    private OAuth2ProviderEnum providerType;

    @Enumerated(EnumType.STRING)
    private Set<RoleEnum> roles;

    @CreationTimestamp
    @Column(updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

    @Override
    public String getUsername() {
        return this.id;
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .toList();
    }
}