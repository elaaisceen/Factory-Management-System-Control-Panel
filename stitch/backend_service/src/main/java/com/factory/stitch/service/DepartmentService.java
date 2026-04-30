package com.factory.stitch.service;

import com.factory.stitch.dto.DepartmentSummary;
import com.factory.stitch.dto.ProcessResult;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {

    private final RoleMatrixService roleMatrixService;

    public DepartmentService(RoleMatrixService roleMatrixService) {
        this.roleMatrixService = roleMatrixService;
    }

    public List<DepartmentSummary> getVisibleDepartments(String role) {
        List<DepartmentSummary> departments = new ArrayList<>();
        
        String normalizedRole = roleMatrixService.normalizeRole(role);

        if ("ADMIN".equals(normalizedRole) || "HR".equals(normalizedRole)) {
            DepartmentSummary hr = new DepartmentSummary();
            hr.setCode("HR");
            hr.setDepartmanAdi("İnsan Kaynakları");
            hr.setSorumluPersonel("Sistem Yöneticisi");
            hr.setCalisanSayisi(15);
            hr.setAylikButce(new BigDecimal("150000.00"));
            departments.add(hr);
        }
        
        if ("ADMIN".equals(normalizedRole) || "IT".equals(normalizedRole)) {
            DepartmentSummary it = new DepartmentSummary();
            it.setCode("IT");
            it.setDepartmanAdi("Bilgi İşlem");
            it.setSorumluPersonel("IT Yöneticisi");
            it.setCalisanSayisi(8);
            it.setAylikButce(new BigDecimal("200000.00"));
            departments.add(it);
        }

        if ("ADMIN".equals(normalizedRole) || "PRODUCTION".equals(normalizedRole)) {
            DepartmentSummary prod = new DepartmentSummary();
            prod.setCode("PRODUCTION");
            prod.setDepartmanAdi("Üretim");
            prod.setSorumluPersonel("Üretim Müdürü");
            prod.setCalisanSayisi(120);
            prod.setAylikButce(new BigDecimal("1500000.00"));
            departments.add(prod);
        }
        
        if ("ADMIN".equals(normalizedRole) || "FINANCE".equals(normalizedRole)) {
            DepartmentSummary fin = new DepartmentSummary();
            fin.setCode("FINANCE");
            fin.setDepartmanAdi("Finans");
            fin.setSorumluPersonel("Finans Müdürü");
            fin.setCalisanSayisi(5);
            fin.setAylikButce(new BigDecimal("500000.00"));
            departments.add(fin);
        }

        if ("ADMIN".equals(normalizedRole) || "STOCK".equals(normalizedRole)) {
            DepartmentSummary stock = new DepartmentSummary();
            stock.setCode("STOCK");
            stock.setDepartmanAdi("Stok ve Depo");
            stock.setSorumluPersonel("Depo Sorumlusu");
            stock.setCalisanSayisi(12);
            stock.setAylikButce(new BigDecimal("80000.00"));
            departments.add(stock);
        }

        if ("ADMIN".equals(normalizedRole) || "PURCHASING".equals(normalizedRole)) {
            DepartmentSummary purchasing = new DepartmentSummary();
            purchasing.setCode("PURCHASING");
            purchasing.setDepartmanAdi("Satın Alma");
            purchasing.setSorumluPersonel("Satın Alma Sorumlusu");
            purchasing.setCalisanSayisi(7);
            purchasing.setAylikButce(new BigDecimal("300000.00"));
            departments.add(purchasing);
        }

        return departments;
    }

    public Optional<DepartmentSummary> getDepartmentStatus(String role, String code) {
        return getVisibleDepartments(role).stream()
                .filter(d -> d.getCode().equalsIgnoreCase(code))
                .findFirst();
    }

    public Optional<ProcessResult> runDepartmentProcess(String role, String code) {
        Optional<DepartmentSummary> summary = getDepartmentStatus(role, code);
        if (summary.isEmpty()) {
            return Optional.empty();
        }

        ProcessResult result = new ProcessResult();
        result.setDepartmentCode(summary.get().getCode());
        result.setDepartmentName(summary.get().getDepartmanAdi());
        result.setExecutedBy(role);
        result.setExecutedAt(LocalDateTime.now());
        
        List<String> steps = new ArrayList<>();
        steps.add("Süreç başlatıldı.");
        steps.add("Yetki kontrolleri (" + role + ") başarıyla tamamlandı.");
        steps.add(summary.get().getDepartmanAdi() + " departmanı işlemleri çalıştırılıyor.");
        steps.add("İşlem başarıyla tamamlandı.");
        
        result.setSteps(steps);

        return Optional.of(result);
    }
}
