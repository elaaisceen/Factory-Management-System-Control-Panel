package com.fabrika.model;

/**
 * Uretim sınıfı, fabrikadaki fiziksel üretim süreçlerini yönetir.
 * SatinAlma'da olduğu gibi Department'tan türetilmiş ve SurecYurutulebilir yeteneği eklenmiştir.
 */

public class Uretim extends Department implements SurecYurutulebilir {

    // Constructor: Üst sınıfa "Uretim" adını ve sorumlu kişiyi yönlendirir.

    public Uretim(String sorumluPersonel) {
        super("Uretim", sorumluPersonel);
    }

    /**
     * Üretim öncesi hazırlık aşaması.
     * @param projeKodu Üretilecek ürün grubunun benzersiz kodu.
     */

    public void planlamaYap(String projeKodu) {
        islemAnimasyonu();
        System.out.println(projeKodu + " kodlu uretim projesinin planlamasi yapildi.");
    }

    // Fiziksel montaj aşamasını temsil eder.

    public void montajYap() {
        islemAnimasyonu();
        System.out.println("Montaj hattindaki islemler tamamlandi.");
    }

    // Üretilen ürünlerin standartlara uygunluğunu denetler.

    public void kaliteKontrol() {
        islemAnimasyonu();
        System.out.println("Urunler kalite kontrolden gecti. Sorun yok!");
    }

    // Arayüzden (Interface) gelen bu metot, tüm üretim safhalarını belirli bir mantıksal sırayla (Planlama -> Montaj -> Kontrol) çalıştırır.

    @Override
    public void departmanSureciniYurut() {
        planlamaYap("PRJ-101");
        montajYap();
        kaliteKontrol();
    }
}
