package com.factory.stitch.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "kullanici")
public class Kullanici {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "kullanici_id")
    private Integer id;

    @Column(name = "ad_soyad", nullable = false)
    private String adSoyad;

    @Column(name = "kullanici_adi", nullable = false, unique = true)
    private String kullaniciAdi;

    @Column(name = "sifre_hash", nullable = false)
    private String sifreHash;

    @Column(name = "rol_id", nullable = false)
    private Integer rolId;

    @Column(name = "departman_id")
    private Integer departmanId;

    @Column(name = "aktif", nullable = false)
    private boolean aktif = true;

    @Column(name = "olusturma_tarihi", nullable = false, updatable = false)
    private LocalDateTime olusturmaTarihi = LocalDateTime.now();

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getAdSoyad() { return adSoyad; }
    public void setAdSoyad(String adSoyad) { this.adSoyad = adSoyad; }

    public String getKullaniciAdi() { return kullaniciAdi; }
    public void setKullaniciAdi(String kullaniciAdi) { this.kullaniciAdi = kullaniciAdi; }

    public String getSifreHash() { return sifreHash; }
    public void setSifreHash(String sifreHash) { this.sifreHash = sifreHash; }

    public Integer getRolId() { return rolId; }
    public void setRolId(Integer rolId) { this.rolId = rolId; }

    public Integer getDepartmanId() { return departmanId; }
    public void setDepartmanId(Integer departmanId) { this.departmanId = departmanId; }

    public boolean isAktif() { return aktif; }
    public void setAktif(boolean aktif) { this.aktif = aktif; }

    public LocalDateTime getOlusturmaTarihi() { return olusturmaTarihi; }
    public void setOlusturmaTarihi(LocalDateTime olusturmaTarihi) { this.olusturmaTarihi = olusturmaTarihi; }
}
