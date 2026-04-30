package com.factory.stitch.model;

import java.time.LocalDate;

public class Calisan {
    
    private Long id;
    private String adSoyad;
    private String departman;
    private LocalDate egitimBitisTarihi;
    private boolean videoGonderildi;

    // Boş Constructor
    public Calisan() {}

    // Getter ve Setter Metotları
    public String getAdSoyad() { return adSoyad; }
    public void setAdSoyad(String adSoyad) { this.adSoyad = adSoyad; }

    public String getDepartman() { return departman; }
    public void setDepartman(String departman) { this.departman = departman; }

    public LocalDate getEgitimBitisTarihi() { return egitimBitisTarihi; }
    public void setEgitimBitisTarihi(LocalDate egitimBitisTarihi) { this.egitimBitisTarihi = egitimBitisTarihi; }

    public boolean isVideoGonderildi() { return videoGonderildi; }
    public void setVideoGonderildi(boolean videoGonderildi) { this.videoGonderildi = videoGonderildi; }
}
    

