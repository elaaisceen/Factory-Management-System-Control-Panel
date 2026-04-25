package com.factory.stitch.service;


/**
 * Bu sinif Fabrika ERP backend modulu icin dokumante edilmis Java bileşenidir.
 */
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RoleMatrixService {

    public static final String ROLE_MANAGER = "MUDUR";

    private static final Set<String> DEPARTMENT_CODES = Set.of(
            "IK", "URETIM", "DEPO", "BT", "SATIN_ALMA", "FINANS"
    );

    private final Map<String, Set<String>> roleAccessMap;

    public RoleMatrixService() {
        Map<String, Set<String>> map = new LinkedHashMap<>();
        map.put(ROLE_MANAGER, Set.copyOf(DEPARTMENT_CODES));
        map.put("IK_SORUMLU", Set.of("IK"));
        map.put("URETIM_SORUMLU", Set.of("URETIM"));
        map.put("DEPO_SORUMLU", Set.of("DEPO"));
        map.put("BT_SORUMLU", Set.of("BT"));
        map.put("SATIN_ALMA_SORUMLU", Set.of("SATIN_ALMA"));
        map.put("FINANS_SORUMLU", Set.of("FINANS"));
        this.roleAccessMap = map;
    }

    public String normalizeRole(String role) {
        if (role == null) {
            return "";
        }
        return role.trim().toUpperCase(Locale.ROOT);
    }

    public boolean isKnownRole(String role) {
        return roleAccessMap.containsKey(normalizeRole(role));
    }

    public boolean canAccessDepartment(String role, String departmentCode) {
        String normalizedRole = normalizeRole(role);
        String normalizedDepartmentCode = departmentCode == null
                ? ""
                : departmentCode.trim().toUpperCase(Locale.ROOT);

        Set<String> allowedDepartments = roleAccessMap.get(normalizedRole);
        return allowedDepartments != null && allowedDepartments.contains(normalizedDepartmentCode);
    }

    public Set<String> getVisibleDepartmentCodes(String role) {
        Set<String> allowed = roleAccessMap.get(normalizeRole(role));
        if (allowed == null) {
            return Collections.emptySet();
        }
        return new LinkedHashSet<>(allowed);
    }
}

