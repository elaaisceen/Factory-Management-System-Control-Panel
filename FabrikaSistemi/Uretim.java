package com.fabrika.model;

public class Department {

    protected String departmanAdi;
    protected String sorumluPersonel;
    protected int calisanSayisi;

    public Department(String departmanAdi, String sorumluPersonel) {
        this.departmanAdi = departmanAdi;
        this.sorumluPersonel = sorumluPersonel;
        this.calisanSayisi = 0;
    }

    public String getDepartmanAdi() {
        return departmanAdi;
    }

    public String getSorumluPersonel() {
        return sorumluPersonel;
    }

    protected void islemAnimasyonu() {
        System.out.println("\n[SISTEM] Islem yapiliyor...");
        sleep(400);
        System.out.println("[SISTEM] Bekleyiniz...");
        sleep(400);
    }

    protected void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void durumGoster() {
        System.out.println("\n--- " + departmanAdi + " Departmani Durumu ---");
        System.out.println("Sorumlu: " + sorumluPersonel);
        System.out.println("Calisan Sayisi: " + calisanSayisi);
    }
}
