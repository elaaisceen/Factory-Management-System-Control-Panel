package com.factory.stitch.model;

/**
 * Kisi modeli, sistemdeki temel kullanici bilgisini tasir.
 * OOP acisindan Personel gibi varliklarin ortak alanlarini temsil eder.
 */
public class Kisi {
    private String adSoyad;
    private String rol;

    public Kisi() {
    }

    public Kisi(String adSoyad, String rol) {
        this.adSoyad = adSoyad;
        this.rol = rol;
    }

    public String getAdSoyad() {
        return adSoyad;
    }

    public void setAdSoyad(String adSoyad) {
        this.adSoyad = adSoyad;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}
