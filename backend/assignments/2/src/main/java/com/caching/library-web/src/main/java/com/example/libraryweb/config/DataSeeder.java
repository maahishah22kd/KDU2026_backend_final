package com.example.libraryweb.config;

import com.example.librarydomain.entity.Role;
import com.example.libraryservice.service.UserAccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seedUsers(UserAccountService userAccountService) {
        return args -> {
            // create users only if they don't exist
            try { userAccountService.createUser("member1", "pass123", Role.MEMBER); } catch (Exception ignored) {}
            try { userAccountService.createUser("lib1", "pass123", Role.LIBRARIAN); } catch (Exception ignored) {}
        };
    }
}
