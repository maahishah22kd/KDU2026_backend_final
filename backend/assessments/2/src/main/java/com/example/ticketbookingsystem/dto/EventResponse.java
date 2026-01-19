package com.example.ticketbookingsystem.dto;

import java.time.Instant;
import java.util.UUID;
/**
 * API response representation of an event.
 */
public record EventResponse(
        UUID id,
        String title,
        Instant createdAt,
        Long version
) {}
