package com.factory.stitch.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "stok_hareket")
public class StokHareket {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hareket_id")
    private Integer id;
    
    @Column(name = "malzeme_id", nullable = false)
    private Integer malzemeId;
    
    @Column(name = "kullanici_id", nullable = false)
    private Integer kullaniciId;
    
    @Column(name = "hareket_turu", nullable = false, length = 10)
    private String hareketTuru;
    
    @Column(name = "miktar", nullable = false)
    private Integer miktar;
    
    @Column(name = "hareket_tarihi", nullable = false)
    private LocalDateTime hareketTarihi;
    
    @Column(name = "aciklama", length = 255)
    private String aciklama;

    public StokHareket() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMalzemeId() {
        return malzemeId;
    }

    public void setMalzemeId(Integer malzemeId) {
        this.malzemeId = malzemeId;
    }

    public Integer getKullaniciId() {
        return kullaniciId;
    }

    public void setKullaniciId(Integer kullaniciId) {
        this.kullaniciId = kullaniciId;
    }

    public String getHareketTuru() {
        return hareketTuru;
    }

    public void setHareketTuru(String hareketTuru) {
        this.hareketTuru = hareketTuru;
    }

    public Integer getMiktar() {
        return miktar;
    }

    public void setMiktar(Integer miktar) {
        this.miktar = miktar;
    }

    public LocalDateTime getHareketTarihi() {
        return hareketTarihi;
    }

    public void setHareketTarihi(LocalDateTime hareketTarihi) {
        this.hareketTarihi = hareketTarihi;
    }

    public String getAciklama() {
        return aciklama;
    }

    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }
}
