package com.fabrika.model;

public class SatinAlma extends Department {

    public SatinAlma(String sorumluPersonel) {
        super("Satın Alma", sorumluPersonel);
    }

    public void tedarikciEkle(String firmaAdi) {
        islemAnimasyonu();
        System.out.println(firmaAdi + " isimli tedarikçi sisteme kaydedildi.");
    }

    public void malzemeAl(String malzemeAdi, int miktar) {
        islemAnimasyonu();
        System.out.println(miktar + " adet " + malzemeAdi + " siparişi oluşturuldu.");
    }
}
