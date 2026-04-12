package com.fabrika.model;

public class Depo extends Department {

    public Depo(String sorumluPersonel) {
        super("Depo ve Stok", sorumluPersonel);
    }

    public void stokGirisCikis(String malzemeAdi, String islemTuru) {
        islemAnimasyonu();
        System.out.println(malzemeAdi + " için depo " + islemTuru + " işlemi yapıldı.");
    }

    public void kritikStokKontrol() {
        islemAnimasyonu();
        // ER diyagramındaki "kritik_limit" alanına atıf
        System.out.println("Kritik stok seviyesindeki malzemeler kontrol edildi. Uyarılar oluşturuldu.");
    }
}
