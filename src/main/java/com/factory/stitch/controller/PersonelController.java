package com.factory.stitch.controller;

/**
 * FABRIKA YÖNETİM SİSTEMİ - PERSONEL REST API CONTROLLER
 * =====================================================
 * Bu sınıf, personel yönetimi için REST API endpoint'lerini sağlar.
 * Frontend (HTML/JavaScript) ile backend arasındaki köprüdür.
 * 
 * ÖZELLİKLER:
 * - Personel CRUD işlemleri (Create, Read, Update, Delete)
 * - Departman bazlı personel filtreleme
 * - Detaylı loglama ve hata yönetimi
 * - JSON formatında API yanıtları (ApiResponse wrapper)
 * 
 * API ENDPOINT'LERİ:
 * - GET    /api/personel           -> Tüm personeli listele
 * - GET    /api/personel/{id}      -> ID'ye göre personel getir
 * - GET    /api/personel/departman/{departmanId} -> Departman personeli
 * - POST   /api/personel           -> Yeni personel ekle
 * - PUT    /api/personel/{id}      -> Personel güncelle
 * - DELETE /api/personel/{id}      -> Personel sil
 * 
 * GÜVENLİK:
 * - @CrossOrigin(origins = "*") -> Tüm domain'lerden erişime izin verir
 *   (Geliştirme ortamı için, production'da kısıtlanmalı)
 * 
 * DEĞİŞİKLİK GEÇMİŞİ:
 * - ID tipi Long'dan Integer'a çevrildi
 * - Detaylı loglama eklendi (debugging için)
 * - Varsayılan değer atamaları eklendi
 * 
 * @author Cascade AI Assistant
 * @version 1.1
 */

import com.factory.stitch.dto.ApiResponse;
import com.factory.stitch.model.Personel;
import com.factory.stitch.repository.PersonelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

// @RestController: Bu sınıfın REST API controller olduğunu belirtir
// Spring, bu sınıftaki metodların JSON yanıt döndürdüğünü bilir
@RestController

// @RequestMapping: Tüm endpoint'lerin ön ekini belirler
// Örnek: /api/personel + /{id} = /api/personel/123
@RequestMapping("/api/personel")

// @CrossOrigin: CORS (Cross-Origin Resource Sharing) politikası
// origins = "*": Herhangi bir domain'den gelen isteklere izin verir
@CrossOrigin(origins = "*")
public class PersonelController {

    /**
     * LOGGER - Sistem olaylarını kaydetmek için
     * DEBUG için önemli: Personel kaydetme sürecini adım adım izler
     * Seviyeler: INFO (bilgi), SEVERE (hata), WARNING (uyarı)
     */
    private static final Logger logger = Logger.getLogger(PersonelController.class.getName());

    /**
     * REPOSITORY - Veritabanı erişim katmanı
     * @Autowired: Spring otomatik olarak PersonelRepository bean'ini enjekte eder
     * CRUD işlemleri bu repository üzerinden yapılır
     */
    @Autowired
    private PersonelRepository personelRepository;

    /**
     * TÜM PERSONELİ LİSTELE
     * HTTP Method: GET
     * URL: /api/personel
     * 
     * Kullanım: HR dashboard personel tablosunu doldurmak için
     * Yanıt: { success: true, message: "...", data: [personel listesi] }
     * 
     * @return ApiResponse içinde personel listesi
     */
    @GetMapping
    public ApiResponse<List<Personel>> getAllPersonel() {
        List<Personel> personelList = personelRepository.findAll();
        return new ApiResponse<>(true, "Personel listesi alındı", personelList);
    }

    /**
     * ID'YE GÖRE PERSONEL GETİR
     * HTTP Method: GET
     * URL: /api/personel/{id}
     * 
     * @param id Personel ID'si (URL'den alınır)
     * @PathVariable: URL'deki {id} değerini metod parametresine bağlar
     * @return ApiResponse içinde personel veya hata mesajı
     */
    @GetMapping("/{id}")
    public ApiResponse<Personel> getPersonelById(@PathVariable Integer id) {
        return personelRepository.findById(id)
                .map(personel -> new ApiResponse<>(true, "Personel bulundu", personel))
                .orElse(new ApiResponse<>(false, "Personel bulunamadı", null));
    }

    /**
     * DEPARTMANA GÖRE PERSONEL GETİR
     * HTTP Method: GET
     * URL: /api/personel/departman/{departmanId}
     * 
     * Kullanım: Departman bazlı personel filtreleme
     * Örnek: /api/personel/departman/1 -> Ar-Ge departmanı personeli
     * 
     * @param departmanId Departman ID'si
     * @return ApiResponse içinde filtrelenmiş personel listesi
     */
    @GetMapping("/departman/{departmanId}")
    public ApiResponse<List<Personel>> getPersonelByDepartman(@PathVariable Integer departmanId) {
        List<Personel> personelList = personelRepository.findByDepartmanId(departmanId);
        return new ApiResponse<>(true, "Departman personeli alındı", personelList);
    }

    /**
     * YENİ PERSONEL EKLE
     * HTTP Method: POST
     * URL: /api/personel
     * Content-Type: application/json
     * 
     * Kullanım: HR dashboard'dan yeni personel kaydı oluşturma
     * İstek Gövdesi (Request Body): { "adi": "Ahmet", "soyadi": "Yılmaz", ... }
     * 
     * İŞLEM ADIMLARI:
     * 1. Gelen JSON'u Personel objesine dönüştür (@RequestBody)
     * 2. Eksik alanlara varsayılan değerler ata
     * 3. Veritabanına kaydet
     * 4. Sonucu API yanıtı olarak döndür
     * 
     * VARSAYILAN DEĞERLER:
     * - calismaBaslangic: Şu anki zaman
     * - calismaBitis: 8 saat sonrası
     * - aktif: true
     * - maas: 0 (BigDecimal.ZERO)
     * 
     * @param personel İstek gövdesinden gelen Personel objesi
     * @RequestBody: HTTP istek gövdesini Java objesine dönüştürür
     * @return ApiResponse içinde kaydedilmiş personel veya hata
     */
    @PostMapping
    public ApiResponse<Personel> createPersonel(@RequestBody Personel personel) {
        // DEBUG: Gelen veriyi logla
        logger.info("[PERSONEL] createPersonel cagrildi - adi: " + personel.getAdi() + ", soyadi: " + personel.getSoyadi());
        try {
            // VARSAYILAN DEĞERLER - Eksik alanları doldur
            if (personel.getCalismaBaslangic() == null) {
                personel.setCalismaBaslangic(LocalDateTime.now());
            }
            if (personel.getCalismaBitis() == null) {
                personel.setCalismaBitis(LocalDateTime.now().plusHours(8)); // 8 saat mesai
            }
            if (personel.getAktif() == null) {
                personel.setAktif(true); // Yeni personel varsayılan olarak aktif
            }
            if (personel.getMaas() == null) {
                personel.setMaas(BigDecimal.ZERO); // Varsayılan maaş 0
            }
            
            // VERİTABANINA KAYDET
            logger.info("[PERSONEL] Kaydediliyor...");
            Personel savedPersonel = personelRepository.save(personel);
            logger.info("[PERSONEL] Kaydedildi - ID: " + savedPersonel.getId());
            
            return new ApiResponse<>(true, "Personel başarıyla eklendi", savedPersonel);
        } catch (Exception e) {
            // HATA YÖNETİMİ - Detaylı log ve kullanıcı dostu mesaj
            logger.severe("[PERSONEL] Hata: " + e.getMessage());
            e.printStackTrace();
            return new ApiResponse<>(false, "Personel eklenirken hata: " + e.getMessage(), null);
        }
    }

    /**
     * PERSONEL GÜNCELLE
     * HTTP Method: PUT
     * URL: /api/personel/{id}
     * Content-Type: application/json
     * 
     * Kullanım: Mevcut personel kaydını güncelleme
     * Tüm alanlar güncellenir (PUT = tam güncelleme)
     * 
     * @param id Güncellenecek personel ID'si
     * @param personel Yeni personel bilgileri
     * @return ApiResponse içinde güncellenmiş personel veya hata
     */
    @PutMapping("/{id}")
    public ApiResponse<Personel> updatePersonel(@PathVariable Integer id, @RequestBody Personel personel) {
        return personelRepository.findById(id)
                .map(existingPersonel -> {
                    // Mevcut kaydın tüm alanlarını güncelle
                    existingPersonel.setAdi(personel.getAdi());
                    existingPersonel.setSoyadi(personel.getSoyadi());
                    existingPersonel.setDepartmanId(personel.getDepartmanId());
                    existingPersonel.setMaas(personel.getMaas());
                    existingPersonel.setIseGirisTarihi(personel.getIseGirisTarihi());
                    existingPersonel.setCalismaBaslangic(personel.getCalismaBaslangic());
                    existingPersonel.setCalismaBitis(personel.getCalismaBitis());
                    existingPersonel.setAktif(personel.getAktif());
                    Personel updatedPersonel = personelRepository.save(existingPersonel);
                    return new ApiResponse<>(true, "Personel güncellendi", updatedPersonel);
                })
                .orElse(new ApiResponse<>(false, "Personel bulunamadı", null));
    }

    /**
     * PERSONEL SİL
     * HTTP Method: DELETE
     * URL: /api/personel/{id}
     * 
     * Kullanım: Personel kaydını kalıcı olarak silme
     * DİKKAT: Bu işlem geri alınamaz!
     * 
     * @param id Silinecek personel ID'si
     * @return ApiResponse başarılı/başarısız durumu
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deletePersonel(@PathVariable Integer id) {
        if (personelRepository.existsById(id)) {
            personelRepository.deleteById(id);
            return new ApiResponse<>(true, "Personel silindi", null);
        }
        return new ApiResponse<>(false, "Personel bulunamadı", null);
    }
}
