package com.example.hospital.Service;

import com.example.hospital.Repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class TenantOnboardingService {

    private final ShiftTypeJdbcRepository shiftTypeRepo;
    private final ShiftJdbcRepository shiftRepo;
    private final UserJdbcRepository userRepo;

    public TenantOnboardingService(
            ShiftTypeJdbcRepository shiftTypeRepo,
            ShiftJdbcRepository shiftRepo,
            UserJdbcRepository userRepo) {
        this.shiftTypeRepo = shiftTypeRepo;
        this.shiftRepo = shiftRepo;
        this.userRepo = userRepo;
    }

    @Transactional
    public void onboard(UUID tenantId) {

        UUID morning = shiftTypeRepo.save(tenantId, "Morning");
        shiftRepo.save(morning, tenantId);

        userRepo.save("doctor_ok", "Asia/Kolkata", tenantId);

        // FORCE FAILURE (timezone is NOT NULL)
        userRepo.save("doctor_fail", null, tenantId);
    }
}


