package com.fabrika.model;

public class It extends Department implements SurecYurutulebilir {

    public It(String sorumluPersonel) {
        super("IT", sorumluPersonel);
    }

    public void sistemYonetimi() {
        islemAnimasyonu();
        System.out.println("Sunucular ve veritabani baglantilari kontrol edildi. Sistem aktif.");
    }

    public void veriYedekle() {
        islemAnimasyonu();
        System.out.println("Fabrika verileri guvenli sekilde yedeklendi.");
    }

    @Override
    public void departmanSureciniYurut() {
        sistemYonetimi();
        veriYedekle();
    }
}
