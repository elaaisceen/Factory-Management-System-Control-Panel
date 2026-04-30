package com.factory.stitch.controller;

import com.factory.stitch.dto.ApiResponse;
import com.factory.stitch.model.MuhasebeKayit;
import com.factory.stitch.repository.MuhasebeKayitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/muhasebe-kayit")
@CrossOrigin(origins = "*")
public class MuhasebeKayitController {

    @Autowired
    private MuhasebeKayitRepository muhasebeKayitRepository;

    @GetMapping
    public ApiResponse<List<MuhasebeKayit>> getAllMuhasebeKayit() {
        List<MuhasebeKayit> kayitList = muhasebeKayitRepository.findAll();
        return new ApiResponse<>(true, "Muhasebe kayıt listesi alındı", kayitList);
    }

    @GetMapping("/{id}")
    public ApiResponse<MuhasebeKayit> getMuhasebeKayitById(@PathVariable Integer id) {
        return muhasebeKayitRepository.findById(id)
                .map(kayit -> new ApiResponse<>(true, "Muhasebe kayıt bulundu", kayit))
                .orElse(new ApiResponse<>(false, "Muhasebe kayıt bulunamadı", null));
    }

    @GetMapping("/departman/{departmanId}")
    public ApiResponse<List<MuhasebeKayit>> getMuhasebeKayitByDepartman(@PathVariable Integer departmanId) {
        List<MuhasebeKayit> kayitList = muhasebeKayitRepository.findByDepartmanId(departmanId);
        return new ApiResponse<>(true, "Departman muhasebe kayıtları alındı", kayitList);
    }

    @PostMapping
    public ApiResponse<MuhasebeKayit> createMuhasebeKayit(@RequestBody MuhasebeKayit kayit) {
        try {
            if (kayit.getKayitTarihi() == null) {
                kayit.setKayitTarihi(LocalDateTime.now());
            }
            if (kayit.getTutar() == null) {
                kayit.setTutar(BigDecimal.ZERO);
            }
            
            MuhasebeKayit savedKayit = muhasebeKayitRepository.save(kayit);
            return new ApiResponse<>(true, "Muhasebe kayıt başarıyla eklendi", savedKayit);
        } catch (Exception e) {
            return new ApiResponse<>(false, "Muhasebe kayıt eklenirken hata: " + e.getMessage(), null);
        }
    }

    @PutMapping("/{id}")
    public ApiResponse<MuhasebeKayit> updateMuhasebeKayit(@PathVariable Integer id, @RequestBody MuhasebeKayit kayit) {
        return muhasebeKayitRepository.findById(id)
                .map(existingKayit -> {
                    existingKayit.setDepartmanId(kayit.getDepartmanId());
                    existingKayit.setTutar(kayit.getTutar());
                    existingKayit.setKayitTuru(kayit.getKayitTuru());
                    existingKayit.setKayitTarihi(kayit.getKayitTarihi());
                    existingKayit.setAciklama(kayit.getAciklama());
                    MuhasebeKayit updatedKayit = muhasebeKayitRepository.save(existingKayit);
                    return new ApiResponse<>(true, "Muhasebe kayıt güncellendi", updatedKayit);
                })
                .orElse(new ApiResponse<>(false, "Muhasebe kayıt bulunamadı", null));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteMuhasebeKayit(@PathVariable Integer id) {
        if (muhasebeKayitRepository.existsById(id)) {
            muhasebeKayitRepository.deleteById(id);
            return new ApiResponse<>(true, "Muhasebe kayıt silindi", null);
        }
        return new ApiResponse<>(false, "Muhasebe kayıt bulunamadı", null);
    }
}
