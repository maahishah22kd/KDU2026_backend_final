package com.example.ticketbookingsystem.config;

import com.example.ticketbookingsystem.entity.Role;
import com.example.ticketbookingsystem.service.UserAccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seedUsers(UserAccountService userAccountService) {
        return args -> {
            // create users only if they don't exist
            try { userAccountService.createUser("user1", "pass123", Role.USER); } catch (Exception ignored) {}
            try { userAccountService.createUser("admin1", "pass123", Role.ADMIN); } catch (Exception ignored) {}
        };
    }


}
