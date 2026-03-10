package com.fabrika.model;

public class Depo extends Department implements SurecYurutulebilir {

    public Depo(String sorumluPersonel) {
        super("Depo ve Stok", sorumluPersonel);
    }

    public void stokGirisCikis(String malzemeAdi, String islemTuru) {
        islemAnimasyonu();
        System.out.println(malzemeAdi + " icin depo " + islemTuru + " islemi yapildi.");
    }

    public void kritikStokKontrol() {
        islemAnimasyonu();
        System.out.println("Kritik stok seviyesindeki malzemeler kontrol edildi. Uyarilar olusturuldu.");
    }

    @Override
    public void departmanSureciniYurut() {
        stokGirisCikis("Demir Profil", "GIRIS");
        kritikStokKontrol();
    }
}
