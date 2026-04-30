package com.factory.stitch.service;

import com.factory.stitch.dao.DashboardMetricsDao;
import com.factory.stitch.dto.DashboardSummary;
import org.springframework.stereotype.Service;

@Service
public class DashboardMetricsService {

    private final DashboardMetricsDao dashboardMetricsDao;

    public DashboardMetricsService(DashboardMetricsDao dashboardMetricsDao) {
        this.dashboardMetricsDao = dashboardMetricsDao;
    }

    public DashboardSummary getSummary() {
        return dashboardMetricsDao.getSummary();
    }
}
