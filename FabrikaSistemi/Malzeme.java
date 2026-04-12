package com.fabrika.model;

public class It extends Department {

    public It(String sorumluPersonel) {
        super("IT", sorumluPersonel);
    }

    public void sistemYonetimi() {
        islemAnimasyonu();
        System.out.println("Sunucular ve veritabanı bağlantıları kontrol edildi. Sistem aktif.");
    }

    public void veriYedekle() {
        islemAnimasyonu();
        System.out.println("Fabrika verileri güvenli bir şekilde yedeklendi.");
    }
}
