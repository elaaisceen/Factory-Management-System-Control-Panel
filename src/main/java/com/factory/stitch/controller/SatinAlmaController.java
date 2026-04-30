package com.factory.stitch.controller;

import com.factory.stitch.model.KullaniciOturum;
import com.factory.stitch.model.Rol;
import com.factory.stitch.model.Rol.Departman;
import com.factory.stitch.model.SatinAlma;
import com.factory.stitch.dto.ApiResponse;
import com.factory.stitch.service.ErisimKontrolServisi;
import com.factory.stitch.service.ErisimKontrolServisi.ErisimReddedildiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Satın Alma REST Controller.
 *
 * Spring Security aktifken metodlara şu not eklenir (yorum kaldırılır):
 *   @PreAuthorize("hasAnyRole('GENEL_MUDUR','GELISTIRICI_ADMIN',
 *                   'SATINALMA_PERSONEL','SATINALMA_YONETICI','BT_YONETICI')")
 *
 * Spring Security yokken ErisimKontrolServisi doğrudan kullanılır.
 */
@RestController
@RequestMapping("/api/satinalma")
@CrossOrigin(origins = "*") // Prod ortamda spesifik origin girilmeli
public class SatinAlmaController {

    private final SatinAlma            satinAlmaService = new SatinAlma("Sistem Yoneticisi");
    private final ErisimKontrolServisi erisimKontrol    = new ErisimKontrolServisi();

    // ── Oturum Yardımcısı ──────────────────────────────────────────────────
    // Gerçek uygulamada bu değer SecurityContextHolder veya JWT'den alınır.
    // Şimdilik request header üzerinden basit simülasyon yapılıyor.

    private KullaniciOturum oturumuCoz(Map<String, String> headers) {
        int    rolId  = Integer.parseInt(headers.getOrDefault("X-Rol-Id", "13"));
        int    kulId  = Integer.parseInt(headers.getOrDefault("X-Kullanici-Id", "1"));
        String ad     = headers.getOrDefault("X-Kullanici-Ad", "Demo Kullanici");
        String eposta = headers.getOrDefault("X-Eposta", "demo@fabrika.com");
        return new KullaniciOturum(kulId, ad, eposta, Rol.fromId(rolId));
    }

    // ── Endpoints ──────────────────────────────────────────────────────────

    /**
     * Satın Alma departmanının durumunu döndürür.
     * Erişim: Satın Alma (Personel/Yönetici), IT, Genel Müdür, Admin
     *
     * Spring Security notu:
     *   @PreAuthorize("hasAnyRole('GENEL_MUDUR','GELISTIRICI_ADMIN',
     *                'SATINALMA_PERSONEL','SATINALMA_YONETICI',
     *                'BT_PERSONEL','BT_YONETICI')")
     */
    @GetMapping("/durum")
    public ResponseEntity<ApiResponse> getDurum(@RequestHeader Map<String, String> headers) {
        try {
            KullaniciOturum oturum = oturumuCoz(headers);
            erisimKontrol.dogrulaErisim(oturum, Departman.SATINALMA);
            satinAlmaService.durumGoster();
            return ResponseEntity.ok(new ApiResponse(true,
                    "Satın Alma durumu alındı. (Loglara bakınız)", null));
        } catch (ErisimReddedildiException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse(false, e.getMessage(), null));
        }
    }

    /**
     * Yeni tedarikçi kaydeder.
     * Erişim: Satın Alma Yöneticisi, IT, Genel Müdür, Admin
     *
     * Spring Security notu:
     *   @PreAuthorize("hasAnyRole('GENEL_MUDUR','GELISTIRICI_ADMIN',
     *                'SATINALMA_YONETICI','BT_YONETICI')")
     */
    @PostMapping("/tedarikci")
    public ResponseEntity<ApiResponse> addTedarikci(
            @RequestHeader Map<String, String> headers,
            @RequestBody   Map<String, String> request) {
        try {
            KullaniciOturum oturum = oturumuCoz(headers);
            erisimKontrol.dogrulaYazma(oturum, Departman.SATINALMA);

            String firmaAdi = request.get("firmaAdi");
            if (firmaAdi == null || firmaAdi.isBlank()) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse(false, "Firma adı eksik.", null));
            }
            satinAlmaService.tedarikciEkle(firmaAdi);
            return ResponseEntity.ok(new ApiResponse(true,
                    firmaAdi + " tedarikçisi başarıyla eklendi.", null));
        } catch (ErisimReddedildiException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse(false, e.getMessage(), null));
        }
    }

    /**
     * Satın alma siparişi oluşturur.
     *
     * Spring Security notu:
     *   @PreAuthorize("hasAnyRole('GENEL_MUDUR','GELISTIRICI_ADMIN',
     *                'SATINALMA_PERSONEL','SATINALMA_YONETICI',
     *                'BT_PERSONEL','BT_YONETICI')")
     */
    @PostMapping("/siparis")
    public ResponseEntity<ApiResponse> createSiparis(
            @RequestHeader Map<String, String> headers,
            @RequestBody   Map<String, Object> request) {
        try {
            KullaniciOturum oturum = oturumuCoz(headers);
            erisimKontrol.dogrulaErisim(oturum, Departman.SATINALMA);

            String  malzemeAdi = (String)  request.get("malzemeAdi");
            Integer miktar     = (Integer) request.get("miktar");

            if (malzemeAdi == null || miktar == null || miktar <= 0) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse(false, "Geçersiz sipariş verisi.", null));
            }
            satinAlmaService.malzemeAl(malzemeAdi, miktar);
            return ResponseEntity.ok(new ApiResponse(true,
                    miktar + " adet " + malzemeAdi + " siparişi oluşturuldu.", null));
        } catch (ErisimReddedildiException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse(false, e.getMessage(), null));
        }
    }

    /**
     * Otomatik süreci yürütür — sadece Yöneticiler ve üstü.
     *
     * Spring Security notu:
     *   @PreAuthorize("hasAnyRole('GENEL_MUDUR','GELISTIRICI_ADMIN',
     *                'SATINALMA_YONETICI')")
     */
    @PostMapping("/surec/calistir")
    public ResponseEntity<ApiResponse> executeSurec(
            @RequestHeader Map<String, String> headers) {
        try {
            KullaniciOturum oturum = oturumuCoz(headers);
            erisimKontrol.dogrulaYazma(oturum, Departman.SATINALMA);
            satinAlmaService.departmanSureciniYurut();
            return ResponseEntity.ok(new ApiResponse(true,
                    "Satın alma otomatik süreci yürütüldü.", null));
        } catch (ErisimReddedildiException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse(false, e.getMessage(), null));
        }
    }
}