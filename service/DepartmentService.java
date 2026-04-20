package com.factory.stitch.service;


/**
 * Bu sinif Fabrika ERP backend modulu icin dokumante edilmis Java bileşenidir.
 */
import com.factory.stitch.dto.DepartmentSummary;
import com.factory.stitch.dto.ProcessResult;
import com.factory.stitch.model.*;
import com.factory.stitch.util.DosyaKayitUtil;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class DepartmentService {

    private final RoleMatrixService roleMatrixService;
    private final Map<String, DepartmentContext> departments = new LinkedHashMap<>();

    public DepartmentService(RoleMatrixService roleMatrixService) {
        this.roleMatrixService = roleMatrixService;
        register("IK", new HumanResources("Ahmet Y."), 12, new BigDecimal("350000"));
        register("URETIM", new Uretim("Mehmet K."), 46, new BigDecimal("1250000"));
        register("DEPO", new Depo("Ali Riza"), 18, new BigDecimal("420000"));
        register("BT", new It("Ela Eda"), 9, new BigDecimal("280000"));
        register("SATIN_ALMA", new SatinAlma("Gizem S."), 7, new BigDecimal("300000"));
        register("FINANS", new Finans("Aylin T."), 11, new BigDecimal("500000"));
    }

    public List<DepartmentSummary> getVisibleDepartments(String role) {
        Set<String> visibleCodes = roleMatrixService.getVisibleDepartmentCodes(role);
        List<DepartmentSummary> result = new ArrayList<>();

        for (String code : visibleCodes) {
            DepartmentContext ctx = departments.get(code);
            if (ctx != null) {
                result.add(toSummary(code, ctx));
            }
        }
        return result;
    }

    public Optional<DepartmentSummary> getDepartmentStatus(String role, String departmentCode) {
        String normalizedCode = normalizeCode(departmentCode);
        if (!roleMatrixService.canAccessDepartment(role, normalizedCode)) {
            return Optional.empty();
        }

        DepartmentContext ctx = departments.get(normalizedCode);
        if (ctx == null) {
            return Optional.empty();
        }

        return Optional.of(toSummary(normalizedCode, ctx));
    }

    public Optional<ProcessResult> runDepartmentProcess(String role, String departmentCode) {
        String normalizedCode = normalizeCode(departmentCode);
        if (!roleMatrixService.canAccessDepartment(role, normalizedCode)) {
            return Optional.empty();
        }

        DepartmentContext ctx = departments.get(normalizedCode);
        if (ctx == null) {
            return Optional.empty();
        }

        if (!(ctx.department instanceof SurecYurutulebilir)) {
            return Optional.empty();
        }

        ((SurecYurutulebilir) ctx.department).departmanSureciniYurut();
        if ("IK".equals(normalizedCode)) {
            ctx.calisanSayisi += 1;
        }

        ProcessResult result = new ProcessResult();
        result.setDepartmentCode(normalizedCode);
        result.setDepartmentName(ctx.department.getDepartmanAdi());
        result.setExecutedBy(ctx.department.getSorumluPersonel());
        result.setExecutedAt(LocalDateTime.now());
        result.setSteps(buildSteps(normalizedCode));

        DosyaKayitUtil.logSatiriEkle(String.format(
                "[%s] %s departmani sureci %s tarafindan calistirildi.",
                result.getExecutedAt(),
                result.getDepartmentName(),
                result.getExecutedBy()
        ));

        return Optional.of(result);
    }

    private void register(String code, Department department, int calisanSayisi, BigDecimal aylikButce) {
        DepartmentContext ctx = new DepartmentContext();
        ctx.department = department;
        ctx.calisanSayisi = calisanSayisi;
        ctx.aylikButce = aylikButce;
        departments.put(code, ctx);
    }

    private DepartmentSummary toSummary(String code, DepartmentContext ctx) {
        DepartmentSummary summary = new DepartmentSummary();
        summary.setCode(code);
        summary.setDepartmanAdi(ctx.department.getDepartmanAdi());
        summary.setSorumluPersonel(ctx.department.getSorumluPersonel());
        summary.setCalisanSayisi(ctx.calisanSayisi);
        summary.setAylikButce(ctx.aylikButce);
        return summary;
    }

    private List<String> buildSteps(String departmentCode) {
        switch (departmentCode) {
            case "IK":
                return List.of("Personel ise alimi simule edildi", "Bordro hesaplama adimi tamamlandi");
            case "URETIM":
                return List.of("Planlama adimi tamamlandi", "Montaj adimi tamamlandi", "Kalite kontrol tamamlandi");
            case "DEPO":
                return List.of("Stok giris-cikis adimi tamamlandi", "Kritik stok kontrolu tamamlandi");
            case "BT":
                return List.of("Sunucu ve sistem kontrolu tamamlandi", "Veri yedekleme tamamlandi");
            case "SATIN_ALMA":
                return List.of("Tedarikci kaydi olusturuldu", "Malzeme siparisi olusturuldu");
            case "FINANS":
                return List.of("Aylik butce planlandi", "Muhasebe kayitlari guncellendi");
            default:
                return List.of("Genel surec adimi tamamlandi");
        }
    }

    private String normalizeCode(String code) {
        if (code == null) {
            return "";
        }
        return code.trim().toUpperCase(Locale.ROOT);
    }

    private static class DepartmentContext {
        private Department department;
        private int calisanSayisi;
        private BigDecimal aylikButce;
    }
}

