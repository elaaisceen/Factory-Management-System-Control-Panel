package com.factory.stitch.model;

/**
 * FABRIKA YÖNETİM SİSTEMİ - PERSONEL MODÜLÜ
 * ==========================================
 * Bu sınıf, fabrika personelinin veritabanı modelini temsil eder.
 * JPA (Java Persistence API) kullanarak MySQL veritabanındaki 'personel' 
 * tablosu ile eşleştirilmiştir.
 * 
 * ÖZELLİKLER:
 * - Personel kayıtlarının saklanması (ad, soyad, maaş, vb.)
 * - Departman ilişkilendirmesi (departmanId ile)
 * - Çalışma saatleri takibi (başlangıç-bitiş)
 * - Aktif/pasif durum yönetimi
 * 
 * DEĞİŞİKLİK GEÇMİŞİ:
 * - ID tipi Long'dan Integer'a çevrildi (MySQL INT tipi uyumluluğu için)
 * - Otomatik artan (auto-increment) primary key kullanımı
 * 
 * @author Cascade AI Assistant
 * @version 1.1
 */

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

// JPA Entity anotasyonu - bu sınıfın veritabanı tablosuna karşılık geldiğini belirtir
@Entity
// Veritabanı tablo adı ve özellikleri
@Table(name = "personel")
public class Personel {
    
    /**
     * PERSONEL ID (Birincil Anahtar)
     * - Otomatik artan (IDENTITY) stratejisi kullanılır
     * - MySQL'deki AUTO_INCREMENT'e karşılık gelir
     * - Integer tipi: MySQL INT tipiyle uyumlu
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "personel_id")
    private Integer id;
    
    /**
     * DEPARTMAN ID (Yabancı Anahtar)
     * - Personelin bağlı olduğu departmanın ID'si
     * - 'departman' tablosuyla ilişki kurar
     * - nullable = false: Her personel bir departmana atanmalı
     */
    @Column(name = "departman_id", nullable = false)
    private Integer departmanId;
    
    /**
     * PERSONEL ADI
     * - nullable = false: Boş bırakılamaz
     * - length = 100: Maksimum 100 karakter
     */
    @Column(name = "adi", nullable = false, length = 100)
    private String adi;
    
    /**
     * PERSONEL SOYADI
     * - nullable = false: Boş bırakılamaz
     * - length = 100: Maksimum 100 karakter
     */
    @Column(name = "soyadi", nullable = false, length = 100)
    private String soyadi;
    
    /**
     * MAAŞ BİLGİSİ
     * - precision = 12: Toplam 12 basamak
     * - scale = 2: Ondalık kısımda 2 basamak (kuruş)
     * - BigDecimal: Hassas para hesaplamaları için
     * - Örnek: 9999999999.99 (10 milyar TL'ye kadar)
     */
    @Column(name = "maas", nullable = false, precision = 12, scale = 2)
    private BigDecimal maas;
    
    /**
     * İŞE GİRİŞ TARİHİ
     * - LocalDate: Sadece tarih bilgisi (saat yok)
     * - Personelin işe başladığı gün
     */
    @Column(name = "ise_giris_tarihi", nullable = false)
    private LocalDate iseGirisTarihi;
    
    /**
     * ÇALIŞMA BAŞLANGIÇ SAATİ
     * - LocalDateTime: Tarih + saat bilgisi
     * - Günlük mesai başlangıç zamanı
     */
    @Column(name = "calisma_baslangic", nullable = false)
    private LocalDateTime calismaBaslangic;
    
    /**
     * ÇALIŞMA BİTİŞ SAATİ
     * - LocalDateTime: Tarih + saat bilgisi
     * - Günlük mesai bitiş zamanı
     */
    @Column(name = "calisma_bitis", nullable = false)
    private LocalDateTime calismaBitis;
    
    /**
     * AKTİF/PASİF DURUMU
     * - true: Personel aktif çalışıyor
     * - false: Personel pasif/ayrılmış
     * - Varsayılan değer: true (yeni kayıtlar aktif)
     */
    @Column(name = "aktif", nullable = false)
    private Boolean aktif = true;

    /**
     * BOŞ CONSTRUCTOR
     * - JPA'nın entity sınıfları için gereklidir
     * - Parametresiz nesne oluşturmayı sağlar
     */
    public Personel() {
    }

    /**
     * CONSTRUCTOR (Ad ve Soyad ile)
     * - Hızlı personel oluşturma için kullanılır
     * 
     * @param adi Personelin adı
     * @param soyadi Personelin soyadı
     */
    public Personel(String adi, String soyadi) {
        this.adi = adi;
        this.soyadi = soyadi;
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

    public String getAdi() {
        return adi;
    }

    public void setAdi(String adi) {
        this.adi = adi;
    }

    public String getSoyadi() {
        return soyadi;
    }

    public void setSoyadi(String soyadi) {
        this.soyadi = soyadi;
    }

    public BigDecimal getMaas() {
        return maas;
    }

    public void setMaas(BigDecimal maas) {
        this.maas = maas;
    }

    public LocalDate getIseGirisTarihi() {
        return iseGirisTarihi;
    }

    public void setIseGirisTarihi(LocalDate iseGirisTarihi) {
        this.iseGirisTarihi = iseGirisTarihi;
    }

    public LocalDateTime getCalismaBaslangic() {
        return calismaBaslangic;
    }

    public void setCalismaBaslangic(LocalDateTime calismaBaslangic) {
        this.calismaBaslangic = calismaBaslangic;
    }

    public LocalDateTime getCalismaBitis() {
        return calismaBitis;
    }

    public void setCalismaBitis(LocalDateTime calismaBitis) {
        this.calismaBitis = calismaBitis;
    }

    public Boolean getAktif() {
        return aktif;
    }

    public void setAktif(Boolean aktif) {
        this.aktif = aktif;
    }
}
