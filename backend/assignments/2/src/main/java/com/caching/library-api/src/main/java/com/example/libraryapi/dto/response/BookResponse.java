package com.example.libraryapi.dto.response;

import java.time.Instant;
import java.util.UUID;
/**
 * API response representation of a book.
 *
 * <p>This DTO is intentionally decoupled from the persistence entity to avoid exposing internal
 * fields/relationships and to keep the API contract stable.</p>
 */
public record BookResponse(
        UUID id,
        String title,
        String status,
        Instant createdAt,
        Instant updatedAt,
        Long version
) {}
