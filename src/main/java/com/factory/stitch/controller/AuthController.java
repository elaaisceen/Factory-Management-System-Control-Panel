package com.factory.stitch.controller;

/**
 * Bu sinif Fabrika ERP backend modulu icin dokumante edilmis Java bileşenidir.
 */
import com.factory.stitch.dto.ApiResponse;
import com.factory.stitch.dto.LoginRequest;
import com.factory.stitch.dto.RegisterRequest;
import com.factory.stitch.service.DepartmentService;
import com.factory.stitch.service.RoleMatrixService;
import com.factory.stitch.service.UserService;
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
    private final UserService userService;

    public AuthController(RoleMatrixService roleMatrixService, DepartmentService departmentService, UserService userService) {
        this.roleMatrixService = roleMatrixService;
        this.departmentService = departmentService;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody RegisterRequest request) {
        try {
            // Sadece yönetici rolü kayıt yapabilir
            if (!"ADMIN".equals(request.getAdminRole()) || !"admin123".equals(request.getAdminPassword())) {
                return ResponseEntity.status(403).body(new ApiResponse(false, "Kayıt yapmak için yönetici yetkisi gereklidir."));
            }

            if (request == null || request.getUsername() == null || request.getUsername().isBlank()) {
                return ResponseEntity.badRequest().body(new ApiResponse(false, "Kullanıcı adı zorunludur."));
            }

            if (request.getPassword() == null || request.getPassword().length() < 4) {
                return ResponseEntity.badRequest().body(new ApiResponse(false, "Şifre en az 4 karakter olmalıdır."));
            }

            if (request.getRole() == null || request.getRole().isBlank()) {
                return ResponseEntity.badRequest().body(new ApiResponse(false, "Rol bilgisi zorunludur."));
            }

            String normalizedRole = roleMatrixService.normalizeRole(request.getRole());
            if (!roleMatrixService.isKnownRole(normalizedRole)) {
                return ResponseEntity.status(403).body(new ApiResponse(false, "Geçersiz rol veya yetkisiz erişim."));
            }

            // Kullanıcıyı oluştur
            boolean created = userService.createUser(request.getUsername(), request.getPassword(), normalizedRole);
            if (!created) {
                return ResponseEntity.status(409).body(new ApiResponse(false, "Kullanıcı zaten mevcut."));
            }

            return ResponseEntity.ok(new ApiResponse(true, "Kullanıcı başarıyla oluşturuldu."));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse(false, "Kayıt sırasında hata: " + e.getMessage()));
        }
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

    @PostMapping("/create-admin")
    public ResponseEntity<ApiResponse> createAdmin(@RequestBody Map<String, String> request) {
        try {
            String adminKey = request.get("adminKey");
            if (!"FABRIKA_ADMIN_2024".equals(adminKey)) {
                return ResponseEntity.status(403).body(new ApiResponse(false, "Geçersiz yönetici anahtarı."));
            }

            String username = request.get("username");
            String password = request.get("password");

            if (username == null || username.isBlank() || password == null || password.isBlank()) {
                return ResponseEntity.badRequest().body(new ApiResponse(false, "Kullanıcı adı ve şifre zorunludur."));
            }

            boolean created = userService.createUser(username, password, "ADMIN");
            if (!created) {
                return ResponseEntity.status(409).body(new ApiResponse(false, "Yönetici zaten mevcut."));
            }

            return ResponseEntity.ok(new ApiResponse(true, "Yönetici hesabı başarıyla oluşturuldu."));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse(false, "Hata: " + e.getMessage()));
        }
    }
}
