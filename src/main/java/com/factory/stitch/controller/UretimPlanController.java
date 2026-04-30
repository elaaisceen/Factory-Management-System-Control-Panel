package com.factory.stitch.controller;

import com.factory.stitch.dto.ApiResponse;
import com.factory.stitch.model.UretimPlan;
import com.factory.stitch.repository.UretimPlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/uretim-plan")
@CrossOrigin(origins = "*")
public class UretimPlanController {

    @Autowired
    private UretimPlanRepository uretimPlanRepository;

    @GetMapping
    public ApiResponse<List<UretimPlan>> getAllUretimPlan() {
        List<UretimPlan> planList = uretimPlanRepository.findAll();
        return new ApiResponse<>(true, "Üretim plan listesi alındı", planList);
    }

    @GetMapping("/{id}")
    public ApiResponse<UretimPlan> getUretimPlanById(@PathVariable Integer id) {
        return uretimPlanRepository.findById(id)
                .map(plan -> new ApiResponse<>(true, "Üretim plan bulundu", plan))
                .orElse(new ApiResponse<>(false, "Üretim plan bulunamadı", null));
    }

    @GetMapping("/departman/{departmanId}")
    public ApiResponse<List<UretimPlan>> getUretimPlanByDepartman(@PathVariable Integer departmanId) {
        List<UretimPlan> planList = uretimPlanRepository.findByDepartmanId(departmanId);
        return new ApiResponse<>(true, "Departman üretim planları alındı", planList);
    }

    @PostMapping
    public ApiResponse<UretimPlan> createUretimPlan(@RequestBody UretimPlan plan) {
        try {
            if (plan.getPlanTarihi() == null) {
                plan.setPlanTarihi(LocalDate.now());
            }
            if (plan.getDurum() == null) {
                plan.setDurum("PLANLANDI");
            }
            
            UretimPlan savedPlan = uretimPlanRepository.save(plan);
            return new ApiResponse<>(true, "Üretim plan başarıyla eklendi", savedPlan);
        } catch (Exception e) {
            return new ApiResponse<>(false, "Üretim plan eklenirken hata: " + e.getMessage(), null);
        }
    }

    @PutMapping("/{id}")
    public ApiResponse<UretimPlan> updateUretimPlan(@PathVariable Integer id, @RequestBody UretimPlan plan) {
        return uretimPlanRepository.findById(id)
                .map(existingPlan -> {
                    existingPlan.setDepartmanId(plan.getDepartmanId());
                    existingPlan.setProjeKodu(plan.getProjeKodu());
                    existingPlan.setPlanTarihi(plan.getPlanTarihi());
                    existingPlan.setDurum(plan.getDurum());
                    UretimPlan updatedPlan = uretimPlanRepository.save(existingPlan);
                    return new ApiResponse<>(true, "Üretim plan güncellendi", updatedPlan);
                })
                .orElse(new ApiResponse<>(false, "Üretim plan bulunamadı", null));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteUretimPlan(@PathVariable Integer id) {
        if (uretimPlanRepository.existsById(id)) {
            uretimPlanRepository.deleteById(id);
            return new ApiResponse<>(true, "Üretim plan silindi", null);
        }
        return new ApiResponse<>(false, "Üretim plan bulunamadı", null);
    }
}
