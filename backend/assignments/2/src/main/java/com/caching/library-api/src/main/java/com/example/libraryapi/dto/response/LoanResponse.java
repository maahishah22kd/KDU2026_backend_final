package com.example.libraryapi.dto.response;

import java.time.Instant;
import java.util.UUID;

/**
 * API response representation of a loan (checkout).
 */
public record LoanResponse(
        UUID id,
        UUID bookId,
        UUID borrowerId,
        Instant borrowedAt,
        Instant returnedAt
) {}
