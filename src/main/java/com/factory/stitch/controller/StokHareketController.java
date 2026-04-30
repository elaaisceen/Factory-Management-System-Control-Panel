package com.factory.stitch.controller;

import com.factory.stitch.dto.ApiResponse;
import com.factory.stitch.model.StokHareket;
import com.factory.stitch.repository.StokHareketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/stok-hareket")
@CrossOrigin(origins = "*")
public class StokHareketController {

    @Autowired
    private StokHareketRepository stokHareketRepository;

    @GetMapping
    public ApiResponse<List<StokHareket>> getAllStokHareket() {
        List<StokHareket> hareketList = stokHareketRepository.findAll();
        return new ApiResponse<>(true, "Stok hareket listesi alındı", hareketList);
    }

    @GetMapping("/{id}")
    public ApiResponse<StokHareket> getStokHareketById(@PathVariable Integer id) {
        return stokHareketRepository.findById(id)
                .map(hareket -> new ApiResponse<>(true, "Stok hareket bulundu", hareket))
                .orElse(new ApiResponse<>(false, "Stok hareket bulunamadı", null));
    }

    @GetMapping("/malzeme/{malzemeId}")
    public ApiResponse<List<StokHareket>> getStokHareketByMalzeme(@PathVariable Integer malzemeId) {
        List<StokHareket> hareketList = stokHareketRepository.findByMalzemeId(malzemeId);
        return new ApiResponse<>(true, "Malzeme stok hareketleri alındı", hareketList);
    }

    @PostMapping
    public ApiResponse<StokHareket> createStokHareket(@RequestBody StokHareket hareket) {
        try {
            if (hareket.getHareketTarihi() == null) {
                hareket.setHareketTarihi(LocalDateTime.now());
            }
            
            StokHareket savedHareket = stokHareketRepository.save(hareket);
            return new ApiResponse<>(true, "Stok hareket başarıyla eklendi", savedHareket);
        } catch (Exception e) {
            return new ApiResponse<>(false, "Stok hareket eklenirken hata: " + e.getMessage(), null);
        }
    }

    @PutMapping("/{id}")
    public ApiResponse<StokHareket> updateStokHareket(@PathVariable Integer id, @RequestBody StokHareket hareket) {
        return stokHareketRepository.findById(id)
                .map(existingHareket -> {
                    existingHareket.setMalzemeId(hareket.getMalzemeId());
                    existingHareket.setKullaniciId(hareket.getKullaniciId());
                    existingHareket.setHareketTuru(hareket.getHareketTuru());
                    existingHareket.setMiktar(hareket.getMiktar());
                    existingHareket.setHareketTarihi(hareket.getHareketTarihi());
                    existingHareket.setAciklama(hareket.getAciklama());
                    StokHareket updatedHareket = stokHareketRepository.save(existingHareket);
                    return new ApiResponse<>(true, "Stok hareket güncellendi", updatedHareket);
                })
                .orElse(new ApiResponse<>(false, "Stok hareket bulunamadı", null));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteStokHareket(@PathVariable Integer id) {
        if (stokHareketRepository.existsById(id)) {
            stokHareketRepository.deleteById(id);
            return new ApiResponse<>(true, "Stok hareket silindi", null);
        }
        return new ApiResponse<>(false, "Stok hareket bulunamadı", null);
    }
}
