package com.example.libraryapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
/**
 * Request payload for creating a new book.
 *
 * <p>On creation, the book begins in {@code PROCESSING} state and is not borrowable
 * until cataloging is completed.</p>
 *
 * @param title human-readable title of the book (validated; must meet minimum length rules)
 */
public record CreateBookRequest(
        @NotBlank(message = "must not be blank")
        @Size(min = 2, max = 200, message = "must be between 2 and 200 chars")
        String title
) {}
