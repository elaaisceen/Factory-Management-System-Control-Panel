package com.factory.stitch.controller;

import com.factory.stitch.dto.ApiResponse;
import com.factory.stitch.model.SatinAlmaTalep;
import com.factory.stitch.repository.SatinAlmaTalepRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/satin-alma-talep")
@CrossOrigin(origins = "*")
public class SatinAlmaTalepController {

    @Autowired
    private SatinAlmaTalepRepository satinAlmaTalepRepository;

    @GetMapping
    public ApiResponse<List<SatinAlmaTalep>> getAllSatinAlmaTalep() {
        List<SatinAlmaTalep> talepList = satinAlmaTalepRepository.findAll();
        return new ApiResponse<>(true, "Satın alma talep listesi alındı", talepList);
    }

    @GetMapping("/{id}")
    public ApiResponse<SatinAlmaTalep> getSatinAlmaTalepById(@PathVariable Integer id) {
        return satinAlmaTalepRepository.findById(id)
                .map(talep -> new ApiResponse<>(true, "Satın alma talep bulundu", talep))
                .orElse(new ApiResponse<>(false, "Satın alma talep bulunamadı", null));
    }

    @GetMapping("/departman/{departmanId}")
    public ApiResponse<List<SatinAlmaTalep>> getSatinAlmaTalepByDepartman(@PathVariable Integer departmanId) {
        List<SatinAlmaTalep> talepList = satinAlmaTalepRepository.findByDepartmanId(departmanId);
        return new ApiResponse<>(true, "Departman satın alma talepleri alındı", talepList);
    }

    @PostMapping
    public ApiResponse<SatinAlmaTalep> createSatinAlmaTalep(@RequestBody SatinAlmaTalep talep) {
        try {
            if (talep.getTalepTarihi() == null) {
                talep.setTalepTarihi(LocalDateTime.now());
            }
            if (talep.getDurum() == null) {
                talep.setDurum("BEKLIYOR");
            }
            
            SatinAlmaTalep savedTalep = satinAlmaTalepRepository.save(talep);
            return new ApiResponse<>(true, "Satın alma talep başarıyla eklendi", savedTalep);
        } catch (Exception e) {
            return new ApiResponse<>(false, "Satın alma talep eklenirken hata: " + e.getMessage(), null);
        }
    }

    @PutMapping("/{id}")
    public ApiResponse<SatinAlmaTalep> updateSatinAlmaTalep(@PathVariable Integer id, @RequestBody SatinAlmaTalep talep) {
        return satinAlmaTalepRepository.findById(id)
                .map(existingTalep -> {
                    existingTalep.setDepartmanId(talep.getDepartmanId());
                    existingTalep.setMalzemeId(talep.getMalzemeId());
                    existingTalep.setTalepMiktari(talep.getTalepMiktari());
                    existingTalep.setDurum(talep.getDurum());
                    SatinAlmaTalep updatedTalep = satinAlmaTalepRepository.save(existingTalep);
                    return new ApiResponse<>(true, "Satın alma talep güncellendi", updatedTalep);
                })
                .orElse(new ApiResponse<>(false, "Satın alma talep bulunamadı", null));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteSatinAlmaTalep(@PathVariable Integer id) {
        if (satinAlmaTalepRepository.existsById(id)) {
            satinAlmaTalepRepository.deleteById(id);
            return new ApiResponse<>(true, "Satın alma talep silindi", null);
        }
        return new ApiResponse<>(false, "Satın alma talep bulunamadı", null);
    }
}
