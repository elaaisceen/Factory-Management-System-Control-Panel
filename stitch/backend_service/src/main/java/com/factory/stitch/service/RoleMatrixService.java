package com.factory.stitch.service;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class RoleMatrixService {

    private final List<String> KNOWN_ROLES = Arrays.asList(
            "ADMIN", "MANAGER", "USER", "HR", "IT", "PRODUCTION", "FINANCE", "STOCK", "PURCHASING"
    );

    public boolean isKnownRole(String role) {
        if (role == null || role.trim().isEmpty()) {
            return false;
        }
        return KNOWN_ROLES.contains(role.trim().toUpperCase());
    }

    public String normalizeRole(String role) {
        if (role == null || role.trim().isEmpty()) {
            return "UNKNOWN";
        }
        return role.trim().toUpperCase();
    }
}
