package com.example.ticketbookingsystem.service;

import com.example.ticketbookingsystem.entity.Role;
import com.example.ticketbookingsystem.entity.UserEntity;
import com.example.ticketbookingsystem.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserAccountService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserAccountService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserEntity createUser(String username, String rawPassword, Role role) {
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already exists");
        }
        String hash = passwordEncoder.encode(rawPassword); // Bcrypt
        return userRepository.save(new UserEntity(username, hash, role));
    }
}
