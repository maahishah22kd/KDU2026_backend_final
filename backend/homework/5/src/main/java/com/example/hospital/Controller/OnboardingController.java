package com.example.hospital.Controller;

import com.example.hospital.Service.TenantOnboardingService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/onboard")
public class OnboardingController {

    private final TenantOnboardingService service;

    public OnboardingController(TenantOnboardingService service) {
        this.service = service;
    }

    @PostMapping("/{tenantId}")
    public String onboard(@PathVariable UUID tenantId) {
        service.onboard(tenantId);
        return "Onboarded";
    }
}
