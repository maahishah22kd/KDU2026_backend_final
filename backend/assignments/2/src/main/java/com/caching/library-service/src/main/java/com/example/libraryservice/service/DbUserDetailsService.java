package com.example.libraryservice.service;

import com.example.librarydomain.entity.UserEntity;
import com.example.librarydomain.repo.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Loads application users from the database for Spring Security authentication.
 */

@Service
public class DbUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public DbUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

/**
 * Loads a user by username for authentication.
 */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        // Spring Security expects roles like ROLE_LIBRARIAN / ROLE_MEMBER
        String role = "ROLE_" + user.getRole().name();

        return new User(
                user.getUsername(),
                user.getPasswordHash(),
                user.isEnabled(),
                true, true, true,
                List.of(new SimpleGrantedAuthority(role))
        );
    }
}
