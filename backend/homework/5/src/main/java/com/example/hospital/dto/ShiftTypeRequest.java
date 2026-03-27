package com.example.hospital.dto;

import java.util.UUID;

public record ShiftTypeRequest(
        String name,
        String description,
        UUID tenantId
) {}
