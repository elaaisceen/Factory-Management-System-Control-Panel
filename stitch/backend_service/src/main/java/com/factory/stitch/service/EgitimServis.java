package com.factory.stitch.service;

import com.factory.stitch.model.Calisan;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class EgitimServis {

    public void egitimKontrolEt(List<Calisan> calisanlar) {
        LocalDate bugun = LocalDate.now();

        for (Calisan c : calisanlar) {
            long gunFarki = ChronoUnit.DAYS.between(bugun, c.getEgitimBitisTarihi());

            if (gunFarki <= 7 && gunFarki > 2) {
                System.out.println("BİLDİRİM: " + c.getAdSoyad() + " için eğitim hatırlatması gönderildi.");
            } 
            else if (gunFarki <= 2 && gunFarki >= 0) {
                if (!c.isVideoGonderildi()) {
                    videoGonder(c);
                    c.setVideoGonderildi(true); // Veritabanında güncelle
                }
            }
        }
    }

    private void videoGonder(Calisan c) {
        System.out.println("OTOMATİK SİSTEM: " + c.getAdSoyad() + " personeline eğitim videoları iletildi.");
    }
}