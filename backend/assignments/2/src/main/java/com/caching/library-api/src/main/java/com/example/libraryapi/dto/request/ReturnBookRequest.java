package com.example.libraryapi.dto.request;

import jakarta.validation.constraints.Size;

public record ReturnBookRequest(
        @Size(max = 200, message = "must be <= 200 chars")
        String note
) {}
