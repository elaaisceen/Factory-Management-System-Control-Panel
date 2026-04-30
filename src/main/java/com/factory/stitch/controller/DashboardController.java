package com.factory.stitch.controller;

import com.factory.stitch.dto.ApiResponse;
import com.factory.stitch.dto.DashboardSummary;
import com.factory.stitch.service.DashboardMetricsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {

    private final DashboardMetricsService dashboardMetricsService;

    public DashboardController(DashboardMetricsService dashboardMetricsService) {
        this.dashboardMetricsService = dashboardMetricsService;
    }

    @GetMapping("/summary")
    public ResponseEntity<ApiResponse> getSummary() {
        DashboardSummary summary = dashboardMetricsService.getSummary();
        return ResponseEntity.ok(new ApiResponse(true, "Dashboard SQL ozeti getirildi.", summary));
    }
}
