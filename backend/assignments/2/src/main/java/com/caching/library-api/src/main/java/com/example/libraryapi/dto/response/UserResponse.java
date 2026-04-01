package com.example.libraryapi.dto.response;

import java.time.Instant;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String username,
        String role,
        boolean enabled,
        Instant createdAt
) {}
