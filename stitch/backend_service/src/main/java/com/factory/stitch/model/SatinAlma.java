package com.factory.stitch.model;

/**
 * SatinAlma sınıfı, Department sınıfından miras alır (Inheritance).
 * Aynı zamanda SurecYurutulebilir yeteneğine sahiptir (Interface Implementation).
 */

public class SatinAlma extends Department implements SurecYurutulebilir {

    // Constructor (Yapıcı Metot): Üst sınıfa departman adını ve sorumlu kişiyi gönderir.

    public SatinAlma(String sorumluPersonel) {
        super("Satin Alma", sorumluPersonel);
    }

    // Satın alma departmanına özgü bir işlev: Yeni tedarikçi kaydı.

    public void tedarikciEkle(String firmaAdi) {
        islemAnimasyonu();
        System.out.println(firmaAdi + " isimli tedarikci sisteme kaydedildi.");
    }

    // Malzeme siparişi oluşturma işlevi.

    public void malzemeAl(String malzemeAdi, int miktar) {
        islemAnimasyonu();
        System.out.println(miktar + " adet " + malzemeAdi + " siparisi olusturuldu.");
    }

    /**
     * SurecYurutulebilir arayüzünden (interface) gelen zorunlu metot.
     * Bu departman "çalıştırıldığında" yapılacak otomatik işlem sırasını belirler.
     */

    @Override
    public void departmanSureciniYurut() {
        // Simülasyon gereği otomatik bir akış tanımlanmıştır.
        tedarikciEkle("Demirtas AS");
        malzemeAl("Celik Vida", 5000);
    }
}
