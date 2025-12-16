package com.rbhagat32.auth.backend.repository;

import com.rbhagat32.auth.backend.entity.UserEntity;
import com.rbhagat32.auth.backend.enums.OAuth2ProviderEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByProviderIdAndProviderType(String providerId, OAuth2ProviderEnum providerType);
}