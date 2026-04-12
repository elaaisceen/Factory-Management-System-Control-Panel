package com.fabrika.model;

public class Personel extends BaseEntity {
    private String adSoyad;

    public Personel() {
    }

    public Personel(String adSoyad) {
        this.adSoyad = adSoyad;
    }

    public String getAdSoyad() {
        return adSoyad;
    }

    public void setAdSoyad(String adSoyad) {
        this.adSoyad = adSoyad;
    }
}
