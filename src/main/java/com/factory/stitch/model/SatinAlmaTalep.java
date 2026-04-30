package com.factory.stitch.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "satinalma_talep")
public class SatinAlmaTalep {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "talep_id")
    private Integer id;
    
    @Column(name = "departman_id", nullable = false)
    private Integer departmanId;
    
    @Column(name = "malzeme_id", nullable = false)
    private Integer malzemeId;
    
    @Column(name = "talep_miktari", nullable = false)
    private Integer talepMiktari;
    
    @Column(name = "durum", nullable = false, length = 20)
    private String durum = "BEKLIYOR";
    
    @Column(name = "talep_tarihi", nullable = false)
    private LocalDateTime talepTarihi;

    public SatinAlmaTalep() {
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

    public Integer getMalzemeId() {
        return malzemeId;
    }

    public void setMalzemeId(Integer malzemeId) {
        this.malzemeId = malzemeId;
    }

    public Integer getTalepMiktari() {
        return talepMiktari;
    }

    public void setTalepMiktari(Integer talepMiktari) {
        this.talepMiktari = talepMiktari;
    }

    public String getDurum() {
        return durum;
    }

    public void setDurum(String durum) {
        this.durum = durum;
    }

    public LocalDateTime getTalepTarihi() {
        return talepTarihi;
    }

    public void setTalepTarihi(LocalDateTime talepTarihi) {
        this.talepTarihi = talepTarihi;
    }
}
