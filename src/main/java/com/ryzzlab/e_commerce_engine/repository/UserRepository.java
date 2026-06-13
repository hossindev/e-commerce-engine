package com.ryzzlab.e_commerce_engine.repository;

import com.ryzzlab.e_commerce_engine.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUserId(UUID userId);
}
