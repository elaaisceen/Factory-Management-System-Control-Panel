package com.factory.stitch.dao;

import com.factory.stitch.dto.DepartmentSummary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Repository
public class DepartmentDashboardDao {

    private final JdbcTemplate jdbcTemplate;

    public DepartmentDashboardDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Departman tablosundan dashboard kartlarinda kullanilan ozet veri okunur.
    public List<DepartmentSummary> findAll() {
        String sql = """
                SELECT kod, ad, sorumlu_personel, calisan_sayisi, aylik_butce
                FROM departman
                ORDER BY kod
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            DepartmentSummary summary = new DepartmentSummary();
            summary.setCode(rs.getString("kod"));
            summary.setDepartmanAdi(rs.getString("ad"));
            summary.setSorumluPersonel(rs.getString("sorumlu_personel"));
            summary.setCalisanSayisi(rs.getInt("calisan_sayisi"));
            summary.setAylikButce(rs.getBigDecimal("aylik_butce"));
            return summary;
        });
    }

    public Optional<DepartmentSummary> findByCode(String code) {
        String normalized = code == null ? "" : code.trim().toUpperCase(Locale.ROOT);
        List<DepartmentSummary> rows = jdbcTemplate.query(
                """
                        SELECT kod, ad, sorumlu_personel, calisan_sayisi, aylik_butce
                        FROM departman
                        WHERE kod = ?
                        """,
                (rs, rowNum) -> {
                    DepartmentSummary summary = new DepartmentSummary();
                    summary.setCode(rs.getString("kod"));
                    summary.setDepartmanAdi(rs.getString("ad"));
                    summary.setSorumluPersonel(rs.getString("sorumlu_personel"));
                    summary.setCalisanSayisi(rs.getInt("calisan_sayisi"));
                    summary.setAylikButce(rs.getBigDecimal("aylik_butce"));
                    return summary;
                },
                normalized
        );
        return rows.stream().findFirst();
    }
}
