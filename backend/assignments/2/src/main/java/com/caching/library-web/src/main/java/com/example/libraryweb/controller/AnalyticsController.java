package com.example.libraryweb.controller;

import com.example.libraryapi.dto.response.AuditResponse;
import com.example.libraryservice.service.AnalyticsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/audit")
    public AuditResponse audit() {
        return analyticsService.auditCountsByStatus();
    }
}
