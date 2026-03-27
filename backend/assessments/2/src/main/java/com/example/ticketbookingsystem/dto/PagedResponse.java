package com.example.ticketbookingsystem.dto;

import java.util.List;

/**
 * Standard paginated response wrapper used by list/search endpoints.
 */
public record PagedResponse<T>(
        List<T> items,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean last
) {}
