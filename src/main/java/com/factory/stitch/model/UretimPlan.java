package com.factory.stitch.model;

/**
 * FABRIKA YÖNETİM SİSTEMİ - ÜRETİM PLANLAMA MODÜLÜ
 * ================================================
 * Bu sınıf, fabrika üretim planlarının veritabanı modelini temsil eder.
 * 
 * ÖZELLİKLER:
 * - Üretim projelerinin planlanması ve takibi
 * - Departman bazlı üretim organizasyonu
 * - Durum yönetimi (PLANLANDI, DEVAM, TAMAMLANDI, IPTAL)
 * - Tarih bazlı planlama
 * 
 * KULLANIM ALANLARI:
 * - Üretim dashboard'u
 * - Proje takibi
 * - Departman performans analizi
 * 
 * DURUM DEĞERLERİ:
 * - PLANLANDI: Henüz başlamamış planlar
 * - DEVAM: Şu anda üretimi devam eden planlar
 * - TAMAMLANDI: Bitirilmiş üretimler
 * - IPTAL: İptal edilmiş planlar
 * 
 * @author Cascade AI Assistant
 * @version 1.1
 */

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "uretim_plan")
public class UretimPlan {
    
    /**
     * PLAN ID (Birincil Anahtar)
     * - Otomatik artan ID
     * - MySQL INT tipiyle uyumlu
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plan_id")
    private Integer id;
    
    /**
     * DEPARTMAN ID (Yabancı Anahtar)
     * - Üretimin yapılacağı departman
     * - 'departman' tablosuyla ilişki
     * - nullable = false: Her plan bir departmana atanmalı
     */
    @Column(name = "departman_id", nullable = false)
    private Integer departmanId;
    
    /**
     * PROJE KODU
     * - Üretim projesinin benzersiz kodu
     * - nullable = false: Boş bırakılamaz
     * - length = 50: Maksimum 50 karakter
     * - Örnek: "PRJ-2024-001", "MONTAJ-A345"
     */
    @Column(name = "proje_kodu", nullable = false, length = 50)
    private String projeKodu;
    
    /**
     * PLAN TARİHİ
     * - Üretim planının başlangıç tarihi
     * - LocalDate: Sadece tarih (saat yok)
     * - nullable = false: Tarih belirtilmeli
     */
    @Column(name = "plan_tarihi", nullable = false)
    private LocalDate planTarihi;
    
    /**
     * ÜRETİM DURUMU
     * - Varsayılan değer: "PLANLANDI"
     * - Geçerli değerler: PLANLANDI, DEVAM, TAMAMLANDI, IPTAL
     * - length = 20: Maksimum 20 karakter
     * 
     * NOT: Durum değişiklikleri üretim dashboard'unda takip edilir
     */
    @Column(name = "durum", nullable = false, length = 20)
    private String durum = "PLANLANDI";

    /**
     * BOŞ CONSTRUCTOR
     * - JPA için zorunlu
     */
    public UretimPlan() {
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

    public String getProjeKodu() {
        return projeKodu;
    }

    public void setProjeKodu(String projeKodu) {
        this.projeKodu = projeKodu;
    }

    public LocalDate getPlanTarihi() {
        return planTarihi;
    }

    public void setPlanTarihi(LocalDate planTarihi) {
        this.planTarihi = planTarihi;
    }

    public String getDurum() {
        return durum;
    }

    public void setDurum(String durum) {
        this.durum = durum;
    }
}
