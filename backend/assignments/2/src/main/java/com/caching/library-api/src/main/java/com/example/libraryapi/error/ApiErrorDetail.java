package com.example.libraryapi.error;

/**
 * Field-level error detail for request validation failures.
 */
public record ApiErrorDetail(
        String field,
        String issue
) {}
