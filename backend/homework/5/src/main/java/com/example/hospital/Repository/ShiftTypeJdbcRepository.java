package com.example.hospital.Repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class ShiftTypeJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public ShiftTypeJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public UUID save(UUID tenantId, String name) {
        UUID id = UUID.randomUUID();

        jdbcTemplate.update("""
            INSERT INTO shift_type (id, name, tenant_id)
            VALUES (?, ?, ?)
        """, id.toString(), name, tenantId.toString());

        return id;
    }
}
