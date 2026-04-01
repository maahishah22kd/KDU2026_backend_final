package com.example.libraryapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateCatalogRequest(
        @NotBlank(message = "must not be blank")
        @Size(min = 2, max = 200, message = "must be between 2 and 200 chars")
        String title,

        @NotNull(message = "must not be null")
        Long version
) {}
