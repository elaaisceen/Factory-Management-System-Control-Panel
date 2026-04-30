package com.factory.stitch.model;

/**
 * FABRIKA YÖNETİM SİSTEMİ - MALZEME (STOK) MODÜLÜ
 * ================================================
 * Bu sınıf, fabrika stokunda bulunan malzemelerin/hammaddelerin 
 * veritabanı modelini temsil eder.
 * 
 * ÖZELLİKLER:
 * - Malzeme adı ve birim bilgisi
 * - Stok takibi (mevcut miktar)
 * - Kritik limit belirleme (otomatik uyarı için)
 * - Benzersiz malzeme tanımlaması
 * 
 * KULLANIM ALANLARI:
 * - Stok yönetimi dashboard'u
 * - Satın alma talepleri
 * - Üretim planlaması
 * 
 * DEĞİŞİKLİK GEÇMİŞİ:
 * - ID tipi Long'dan Integer'a çevrildi (MySQL uyumluluğu)
 * - stokMiktari ve kritikLimit Integer yapıldı
 * 
 * @author Cascade AI Assistant
 * @version 1.1
 */

import jakarta.persistence.*;

@Entity
@Table(name = "malzeme")
public class Malzeme {
    
    /**
     * MALZEME ID (Birincil Anahtar)
     * - Otomatik artan (IDENTITY) stratejisi
     * - MySQL INT tipiyle uyumlu
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "malzeme_id")
    private Integer id;
    
    /**
     * MALZEME ADI
     * - unique = true: Aynı isimde malzeme tekrar eklenemez
     * - nullable = false: Boş bırakılamaz
     * - length = 100: Maksimum 100 karakter
     * - Örnek: "Çelik Levha", "Plastik Granül", "Vida M4"
     */
    @Column(name = "malzeme_adi", nullable = false, unique = true, length = 100)
    private String malzemeAdi;
    
    /**
     * STOK MİKTARI
     * - Depodaki mevcut malzeme miktarı
     * - Varsayılan değer: 0 (yeni malzeme için)
     * - Integer tipi: Tam sayı birimler için (ADET, KG, METRE, vb.)
     */
    @Column(name = "stok_miktari", nullable = false)
    private Integer stokMiktari = 0;
    
    /**
     * KRİTİK STOK LİMİTİ
     * - Stok uyarısı için alt limit değeri
     * - stokMiktari < kritikLimit olduğunda sistem uyarı verir
     * - Varsayılan değer: 0
     * - Örnek: Kritik limit 50 ise, stok 49'a düştüğünde alarm verilir
     */
    @Column(name = "kritik_limit", nullable = false)
    private Integer kritikLimit = 0;
    
    /**
     * BİRİM
     * - Malzemenin ölçü birimi
     * - Varsayılan: "ADET"
     * - Diğer örnekler: "KG", "METRE", "LİTRE", "KUTU"
     * - length = 20: Maksimum 20 karakter
     */
    @Column(name = "birim", nullable = false, length = 20)
    private String birim = "ADET";

    /**
     * BOŞ CONSTRUCTOR
     * - JPA için zorunlu
     */
    public Malzeme() {
    }

    /**
     * CONSTRUCTOR (Malzeme adı ile)
     * - Hızlı malzeme oluşturma
     * 
     * @param malzemeAdi Malzemenin adı
     */
    public Malzeme(String malzemeAdi) {
        this.malzemeAdi = malzemeAdi;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMalzemeAdi() {
        return malzemeAdi;
    }

    public void setMalzemeAdi(String malzemeAdi) {
        this.malzemeAdi = malzemeAdi;
    }

    public Integer getStokMiktari() {
        return stokMiktari;
    }

    public void setStokMiktari(Integer stokMiktari) {
        this.stokMiktari = stokMiktari;
    }

    public Integer getKritikLimit() {
        return kritikLimit;
    }

    public void setKritikLimit(Integer kritikLimit) {
        this.kritikLimit = kritikLimit;
    }

    public String getBirim() {
        return birim;
    }

    public void setBirim(String birim) {
        this.birim = birim;
    }
}
