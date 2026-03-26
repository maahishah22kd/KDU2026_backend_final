package com.example.smarthomedevicesystemmanagement.repository;

import com.example.smarthomedevicesystemmanagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByUserName(String userName);

    Optional<User> findByUserNameAndDeletedAtIsNull(String userName);

    Optional<User> findByUserIdAndDeletedAtIsNull(UUID userId);
}
