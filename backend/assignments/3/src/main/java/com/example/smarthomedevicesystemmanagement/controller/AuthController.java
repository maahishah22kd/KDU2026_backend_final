package com.example.smarthomedevicesystemmanagement.controller;

import com.example.smarthomedevicesystemmanagement.config.JwtUtil;
import com.example.smarthomedevicesystemmanagement.dto.LoginRequest;
import com.example.smarthomedevicesystemmanagement.dto.LoginResponse;
import com.example.smarthomedevicesystemmanagement.entity.User;
import com.example.smarthomedevicesystemmanagement.repository.UserRepository;
import com.example.smarthomedevicesystemmanagement.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping
public class AuthController {
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody LoginRequest req){
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.register(req));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest req){
        User user = userRepository.findByUserName(req.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Invalid username or password"));

        if (user == null || !passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid username or password"));
        }
        List<String> roles = List.of("USER");
        String token=jwtUtil.generateToken(user.getUserName(), roles);
        return ResponseEntity.ok(new LoginResponse(token, user.getUserName(), roles));
    }
}
