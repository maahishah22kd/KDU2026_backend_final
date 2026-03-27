package com.example.hospital.dto;

import java.util.UUID;

public record UserRequest(
        String username,
        String timezone,
        UUID tenantId
) {}
