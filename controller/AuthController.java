package com.factory.stitch.controller;


/**
 * Bu sinif Fabrika ERP backend modulu icin dokumante edilmis Java bileşenidir.
 */
import com.factory.stitch.dto.ApiResponse;
import com.factory.stitch.dto.LoginRequest;
import com.factory.stitch.service.DepartmentService;
import com.factory.stitch.service.RoleMatrixService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final RoleMatrixService roleMatrixService;
    private final DepartmentService departmentService;

    public AuthController(RoleMatrixService roleMatrixService, DepartmentService departmentService) {
        this.roleMatrixService = roleMatrixService;
        this.departmentService = departmentService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody LoginRequest request) {
        if (request == null || request.getRole() == null || request.getRole().isBlank()) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Rol bilgisi zorunludur."));
        }

        String normalizedRole = roleMatrixService.normalizeRole(request.getRole());
        if (!roleMatrixService.isKnownRole(normalizedRole)) {
            return ResponseEntity.status(403).body(new ApiResponse(false, "Gecersiz rol veya yetkisiz erisim."));
        }

        String username = request.getUsername();
        if (username == null || username.isBlank()) {
            username = "kullanici";
        }

        String tokenRaw = username + ":" + normalizedRole + ":" + LocalDateTime.now();
        String token = Base64.getEncoder().encodeToString(tokenRaw.getBytes(StandardCharsets.UTF_8));

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("username", username);
        payload.put("role", normalizedRole);
        payload.put("token", token);
        payload.put("menus", departmentService.getVisibleDepartments(normalizedRole));

        return ResponseEntity.ok(new ApiResponse(true, "Giris basarili.", payload));
    }
}

