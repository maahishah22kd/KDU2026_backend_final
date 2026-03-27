package com.example.hospital.Repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class UserJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public UUID save(String username, String timezone, UUID tenantId) {
        UUID id = UUID.randomUUID();

        jdbcTemplate.update("""
            INSERT INTO users (id, username, timezone, tenant_id)
            VALUES (?, ?, ?, ?)
        """, id.toString(), username, timezone, tenantId.toString());

        return id;
    }

    public Map<String, Object> findByTenant(UUID tenantId, int page, int size) {

        int offset = page * size;

        String dataSql = """
            SELECT id, username, timezone, created_at
            FROM users
            WHERE tenant_id = ?
            ORDER BY created_at DESC
            LIMIT ? OFFSET ?
        """;

        List<Map<String, Object>> data =
                jdbcTemplate.queryForList(
                        dataSql,
                        tenantId.toString(),
                        size,
                        offset
                );

        Long total =
                jdbcTemplate.queryForObject(
                        "SELECT COUNT(*) FROM users WHERE tenant_id = ?",
                        Long.class,
                        tenantId.toString()
                );

        Map<String, Object> response = new HashMap<>();
        response.put("data", data);
        response.put("page", page);
        response.put("size", size);
        response.put("totalElements", total);
        response.put("totalPages", (int) Math.ceil((double) total / size));

        return response;
    }
}
