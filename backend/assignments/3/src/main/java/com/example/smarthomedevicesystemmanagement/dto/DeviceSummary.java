package com.example.smarthomedevicesystemmanagement.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record DeviceSummary(
        UUID deviceId,
        String kickstonId,
        String registeredBy,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {}


