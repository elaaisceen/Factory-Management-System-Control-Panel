package com.fabrika.model;

public class Uretim extends Department {

        public Uretim(String sorumluPersonel) {
            super("Üretim", sorumluPersonel);
        }

        public void planlamaYap(String projeKodu) {
            islemAnimasyonu();
            System.out.println(projeKodu + " kodlu üretim projesinin planlaması yapıldı.");
        }

        public void montajYap() {
            islemAnimasyonu();
            System.out.println("Montaj hattındaki işlemler tamamlandı.");
        }

        public void kaliteKontrol() {
            islemAnimasyonu();
            System.out.println("Ürünler kalite kontrolden geçti. Sorun yok!");
        }
    }

