package com.factory.stitch.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "muhasebe_kayit")
public class MuhasebeKayit {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "kayit_id")
    private Integer id;
    
    @Column(name = "departman_id", nullable = false)
    private Integer departmanId;
    
    @Column(name = "tutar", nullable = false, precision = 14, scale = 2)
    private BigDecimal tutar;
    
    @Column(name = "kayit_turu", nullable = false, length = 10)
    private String kayitTuru;
    
    @Column(name = "kayit_tarihi", nullable = false)
    private LocalDateTime kayitTarihi;
    
    @Column(name = "aciklama", length = 255)
    private String aciklama;

    public MuhasebeKayit() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDepartmanId() {
        return departmanId;
    }

    public void setDepartmanId(Integer departmanId) {
        this.departmanId = departmanId;
    }

    public BigDecimal getTutar() {
        return tutar;
    }

    public void setTutar(BigDecimal tutar) {
        this.tutar = tutar;
    }

    public String getKayitTuru() {
        return kayitTuru;
    }

    public void setKayitTuru(String kayitTuru) {
        this.kayitTuru = kayitTuru;
    }

    public LocalDateTime getKayitTarihi() {
        return kayitTarihi;
    }

    public void setKayitTarihi(LocalDateTime kayitTarihi) {
        this.kayitTarihi = kayitTarihi;
    }

    public String getAciklama() {
        return aciklama;
    }

    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }
}
