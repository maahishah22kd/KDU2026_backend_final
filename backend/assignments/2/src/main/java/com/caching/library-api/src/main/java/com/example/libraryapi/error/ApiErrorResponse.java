package com.example.libraryapi.error;

import java.time.Instant;
import java.util.List;

/**
 * Standard error response returned by the API for both expected and unexpected failures.
 */
public record ApiErrorResponse(
        Instant timestamp,
        String path,
        String errorCode,
        String message,
        List<ApiErrorDetail> details,
        String correlationId
) {}
