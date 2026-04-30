package com.factory.stitch.controller;

import com.factory.stitch.dto.ApiResponse;
import com.factory.stitch.model.Bordro;
import com.factory.stitch.service.MaasHesaplamaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bordro")
@CrossOrigin(origins = "*") // HTML / JS frontend'den erişim için
public class BordroController {

    private final MaasHesaplamaService maasService;

    public BordroController(MaasHesaplamaService maasService) {
        this.maasService = maasService;
    }

    /**
     * Frontend maas.html den gelen FETCH POST isteğini yakalar
     */
    @PostMapping("/kaydet")
    public ResponseEntity<ApiResponse> bordroyuKaydet(@RequestBody Bordro payload) {
        try {
            // Service katmanı iş kuralları çalıştırılır (Transaction başlar)
            Bordro kaydedilen = maasService.bordroyuOnaylaVeKaydet(payload);

            return ResponseEntity.ok(new ApiResponse(true, 
                "Bordro başarıyla işlendi ve veritabanına aktarıldı. Personel: " + kaydedilen.getPersonelAd(),
                kaydedilen));

        } catch (Exception e) {
            // Rollback senaryosu
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse(false, "Hata oluştu: İşlem iptal edildi (Rollback). Detay: " + e.getMessage()));
        }
    }
}

