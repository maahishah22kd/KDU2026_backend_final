package com.example.hospital.Repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class ShiftJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public ShiftJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(UUID shiftTypeId, UUID tenantId) {
        jdbcTemplate.update("""
            INSERT INTO shift
            (id, shift_type_id, start_date, end_date, start_time, end_time, tenant_id)
            VALUES (?, ?, CURRENT_DATE, CURRENT_DATE, '08:00', '16:00', ?)
        """, UUID.randomUUID().toString(),
                shiftTypeId.toString(),
                tenantId.toString());
    }
}
