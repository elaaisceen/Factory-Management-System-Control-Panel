package com.factory.stitch.controller;


/**
 * Bu sinif Fabrika ERP backend modulu icin dokumante edilmis Java bileşenidir.
 */
import com.factory.stitch.dto.ApiResponse;
import com.factory.stitch.model.Bordro;
import com.factory.stitch.service.MaasHesaplamaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bordro")
@CrossOrigin(origins = "*")
public class BordroController {

    private final MaasHesaplamaService maasService;

    public BordroController(MaasHesaplamaService maasService) {
        this.maasService = maasService;
    }

    @PostMapping("/kaydet")
    public ResponseEntity<ApiResponse> bordroyuKaydet(@RequestBody Bordro payload) {
        try {
            Bordro kaydedilen = maasService.bordroyuOnaylaVeKaydet(payload);
            return ResponseEntity.ok(new ApiResponse(
                    true,
                    "Bordro basariyla islendi ve veritabanina kaydedildi. Personel: " + kaydedilen.getPersonelAd(),
                    kaydedilen
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ApiResponse(false, "Hata olustu: Islem geri alindi. Detay: " + e.getMessage())
            );
        }
    }
}

