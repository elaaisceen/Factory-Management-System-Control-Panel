package com.factory.stitch.controller;

import com.factory.stitch.dto.ApiResponse;
import com.factory.stitch.model.Malzeme;
import com.factory.stitch.repository.MalzemeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/malzeme")
@CrossOrigin(origins = "*")
public class MalzemeController {

    @Autowired
    private MalzemeRepository malzemeRepository;

    @GetMapping
    public ApiResponse<List<Malzeme>> getAllMalzeme() {
        List<Malzeme> malzemeList = malzemeRepository.findAll();
        return new ApiResponse<>(true, "Malzeme listesi alındı", malzemeList);
    }

    @GetMapping("/{id}")
    public ApiResponse<Malzeme> getMalzemeById(@PathVariable Integer id) {
        return malzemeRepository.findById(id)
                .map(malzeme -> new ApiResponse<>(true, "Malzeme bulundu", malzeme))
                .orElse(new ApiResponse<>(false, "Malzeme bulunamadı", null));
    }

    @GetMapping("/kritik")
    public ApiResponse<List<Malzeme>> getKritikMalzeme() {
        List<Malzeme> kritikList = malzemeRepository.findAll().stream()
                .filter(m -> m.getStokMiktari() <= m.getKritikLimit())
                .toList();
        return new ApiResponse<>(true, "Kritik stok seviyesindeki malzemeler", kritikList);
    }

    @PostMapping
    public ApiResponse<Malzeme> createMalzeme(@RequestBody Malzeme malzeme) {
        try {
            if (malzeme.getStokMiktari() == null) {
                malzeme.setStokMiktari(0);
            }
            if (malzeme.getKritikLimit() == null) {
                malzeme.setKritikLimit(0);
            }
            if (malzeme.getBirim() == null) {
                malzeme.setBirim("ADET");
            }
            
            Malzeme savedMalzeme = malzemeRepository.save(malzeme);
            return new ApiResponse<>(true, "Malzeme başarıyla eklendi", savedMalzeme);
        } catch (Exception e) {
            return new ApiResponse<>(false, "Malzeme eklenirken hata: " + e.getMessage(), null);
        }
    }

    @PutMapping("/{id}")
    public ApiResponse<Malzeme> updateMalzeme(@PathVariable Integer id, @RequestBody Malzeme malzeme) {
        return malzemeRepository.findById(id)
                .map(existingMalzeme -> {
                    existingMalzeme.setMalzemeAdi(malzeme.getMalzemeAdi());
                    existingMalzeme.setStokMiktari(malzeme.getStokMiktari());
                    existingMalzeme.setKritikLimit(malzeme.getKritikLimit());
                    existingMalzeme.setBirim(malzeme.getBirim());
                    Malzeme updatedMalzeme = malzemeRepository.save(existingMalzeme);
                    return new ApiResponse<>(true, "Malzeme güncellendi", updatedMalzeme);
                })
                .orElse(new ApiResponse<>(false, "Malzeme bulunamadı", null));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteMalzeme(@PathVariable Integer id) {
        if (malzemeRepository.existsById(id)) {
            malzemeRepository.deleteById(id);
            return new ApiResponse<>(true, "Malzeme silindi", null);
        }
        return new ApiResponse<>(false, "Malzeme bulunamadı", null);
    }
}
