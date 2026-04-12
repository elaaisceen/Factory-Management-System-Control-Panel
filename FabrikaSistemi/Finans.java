package com.fabrika.model;

public class Department {

        protected String departmanAdi;
        protected String sorumluPersonel;
        protected int calisanSayisi;

        public String getSorumluPersonel() {
            return sorumluPersonel;
        }

        public Department(String departmanAdi, String sorumluPersonel) {
            this.departmanAdi = departmanAdi;
            this.sorumluPersonel = sorumluPersonel;
            this.calisanSayisi = 0; // Başlangıçta 0
        }

        public String getDepartmanAdi() { return departmanAdi; }

        // -----------------------------------------------------------
        // VARSAYILAN METOTLAR (Alt sınıflar override edebilir)
        // -----------------------------------------------------------
        protected void islemAnimasyonu() {
            System.out.println("\n[SİSTEM] İşlem yapılıyor...");
            sleep(500);
            System.out.println("[SİSTEM] Bekleyiniz...");
            sleep(500);
        }

        protected void sleep(int ms) {
            try { Thread.sleep(ms); } catch (Exception e) {}
        }

        public void durumGoster() {
            System.out.println("\n--- " + departmanAdi + " Departmanı Durumu ---");
            System.out.println("Sorumlu: " + sorumluPersonel);
            System.out.println("Çalışan Sayısı: " + calisanSayisi);
        }

        // Alt sınıfların kendine has işlemleri için
        public void ozelIslem() {
            System.out.println(departmanAdi + " için henüz özel bir işlem tanımlanmadı.");
        }
    }
