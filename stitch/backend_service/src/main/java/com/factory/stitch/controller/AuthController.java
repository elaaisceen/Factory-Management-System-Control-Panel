package com.factory.stitch.controller;

import com.factory.stitch.dto.ApiResponse;
import com.factory.stitch.dto.LoginRequest;
import com.factory.stitch.model.Kullanici;
import com.factory.stitch.model.Rol;
import com.factory.stitch.repository.KullaniciRepository;
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
    private final KullaniciRepository kullaniciRepository;

    public AuthController(RoleMatrixService roleMatrixService, 
                          DepartmentService departmentService,
                          KullaniciRepository kullaniciRepository) {
        this.roleMatrixService = roleMatrixService;
        this.departmentService = departmentService;
        this.kullaniciRepository = kullaniciRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody LoginRequest request) {
        if (request == null || request.getUsername() == null || request.getPassword() == null) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Kullanici adi ve sifre zorunludur."));
        }

        var userOpt = kullaniciRepository.findByKullaniciAdi(request.getUsername());
        
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).body(new ApiResponse(false, "Kullanici bulunamadi."));
        }

        Kullanici user = userOpt.get();

        if (!user.getSifreHash().equals(request.getPassword())) {
            return ResponseEntity.status(401).body(new ApiResponse(false, "Hatali sifre."));
        }

        if (!user.isAktif()) {
            return ResponseEntity.status(403).body(new ApiResponse(false, "Hesabiniz pasif durumdadir."));
        }

        Rol rol;
        try {
            rol = Rol.fromId(user.getRolId());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse(false, "Gecersiz rol tanimi."));
        }

        String tokenRaw = user.getKullaniciAdi() + ":" + rol.name() + ":" + LocalDateTime.now();
        String token = Base64.getEncoder().encodeToString(tokenRaw.getBytes(StandardCharsets.UTF_8));

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("userId", user.getId());
        payload.put("username", user.getKullaniciAdi());
        payload.put("fullName", user.getAdSoyad());
        payload.put("roleId", user.getRolId());
        payload.put("roleName", rol.name());
        payload.put("authority", rol.getYetki().name());
        payload.put("department", rol.getDepartman().name());
        payload.put("token", token);
        payload.put("menus", departmentService.getVisibleDepartments(rol.name()));

        return ResponseEntity.ok(new ApiResponse(true, "Giris basarili.", payload));
    }

    @PostMapping("/kayit")
    public ResponseEntity<ApiResponse> kayit(@RequestBody Map<String, Object> request) {
        try {
            String ad = (String) request.get("ad");
            String soyad = (String) request.get("soyad");
            String email = (String) request.get("email");
            String sifre = (String) request.get("sifre");
            Object rolIdObj = request.get("rolId");
            Integer rolId;
            if (rolIdObj instanceof String) {
                rolId = Integer.parseInt((String) rolIdObj);
            } else if (rolIdObj instanceof Number) {
                rolId = ((Number) rolIdObj).intValue();
            } else {
                return ResponseEntity.badRequest().body(new ApiResponse(false, "Gecersiz rol ID'si."));
            }

            if (kullaniciRepository.findByKullaniciAdi(email).isPresent()) {
                return ResponseEntity.badRequest().body(new ApiResponse(false, "Bu e-posta zaten kayitli."));
            }

            Rol rolEnum = Rol.fromId(rolId);

            Kullanici yeniKullanici = new Kullanici();
            yeniKullanici.setAdSoyad(ad + " " + soyad);
            yeniKullanici.setKullaniciAdi(email);
            yeniKullanici.setSifreHash(sifre);
            yeniKullanici.setRolId(rolId);
            yeniKullanici.setDepartmanId(rolEnum.getDepartman().ordinal() + 1);
            yeniKullanici.setAktif(true);

            kullaniciRepository.save(yeniKullanici);
            return ResponseEntity.ok(new ApiResponse(true, "Kayit basarili!"));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(new ApiResponse(false, "Kayit sirasinda hata: " + e.getMessage()));
        }
    }
}
