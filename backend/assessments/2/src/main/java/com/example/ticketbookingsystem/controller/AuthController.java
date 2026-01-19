package com.example.ticketbookingsystem.controller;

import com.example.ticketbookingsystem.JwtUtil;
import com.example.ticketbookingsystem.User;
import com.example.ticketbookingsystem.UserStore;
import com.example.ticketbookingsystem.dto.LoginRequest;
import com.example.ticketbookingsystem.dto.LoginResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class AuthController {

    private final UserStore userStore;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(UserStore userStore, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userStore = userStore;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest dto) {

        User user = userStore.findByUsername(dto.getUsername());

        if (user == null || !passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid username or password"));
        }

        // If roles is List<String> like ["ROLE_ADMIN"]
        List<String> roles = user.getRoles();

        String token = jwtUtil.generateToken(user.getUserName(), roles);

        return ResponseEntity.ok(new LoginResponse(token, user.getUserName(), roles));
    }
}
