package com.example.ticketbookingsystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
/**
 * Request payload for creating a new event.
 */
public record CreateEventRequest(
        @NotBlank(message = "must not be blank")
        @Size(min = 2, max = 200, message = "must be between 2 and 200 chars")
        String title
) {
    public String getTitle() {
        return title();
    }
}
