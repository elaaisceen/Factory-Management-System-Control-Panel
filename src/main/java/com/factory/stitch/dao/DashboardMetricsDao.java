package com.factory.stitch.dao;

import com.factory.stitch.dto.DashboardSummary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;
import java.util.function.Supplier;

@Repository
public class DashboardMetricsDao {

    private final JdbcTemplate jdbcTemplate;

    public DashboardMetricsDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // SQL tablolari uzerinden dashboard ana metrikleri tek noktadan toplanir.
    public DashboardSummary getSummary() {
        DashboardSummary summary = new DashboardSummary();
        summary.setToplamPersonel(safeQuery(() -> queryInt("SELECT COUNT(*) FROM personel"), 0));
        summary.setKritikStokAdedi(safeQuery(() -> queryInt("SELECT COUNT(*) FROM malzeme WHERE kritik_seviye >= mevcut_stok"), 0));
        summary.setAcikSiparisAdedi(safeQuery(() -> queryInt("SELECT COUNT(*) FROM siparis WHERE durum IN ('PENDING','ON_WAY','HAZIRLANIYOR')"), 0));
        summary.setAktifUretimProjesi(safeQuery(() -> queryInt("SELECT COUNT(*) FROM uretim_projesi WHERE durum = 'AKTIF'"), 0));
        summary.setToplamAylikButce(safeQuery(() -> queryDecimal("SELECT COALESCE(SUM(aylik_butce),0) FROM departman"), BigDecimal.ZERO));
        return summary;
    }

    private int queryInt(@NonNull String sql) {
        Integer value = jdbcTemplate.queryForObject(sql, Integer.class);
        return value == null ? 0 : value;
    }

    private BigDecimal queryDecimal(@NonNull String sql) {
        BigDecimal value = jdbcTemplate.queryForObject(sql, BigDecimal.class);
        return value == null ? BigDecimal.ZERO : value;
    }

    private <T> T safeQuery(Supplier<T> supplier, T defaultValue) {
        try {
            return supplier.get();
        } catch (Exception ignored) {
            return defaultValue;
        }
    }
}
