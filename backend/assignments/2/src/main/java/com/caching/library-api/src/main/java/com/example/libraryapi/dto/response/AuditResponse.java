package com.example.libraryapi.dto.response;

import java.util.Map;

/**
 * Aggregated audit summary response.
 *
 * <p>Provides counts grouped by audit status or category. Useful for dashboards and verification
 * of auditing behavior.</p>
 *
 * @param countsByStatus mapping of status/category name to count (e.g., SUCCESS → 10)
 */
public record AuditResponse(
        Map<String, Long> countsByStatus
) {}
