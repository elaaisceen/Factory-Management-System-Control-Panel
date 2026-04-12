package com.fabrika.model;

public class HumanResources extends Department{

        public HumanResources(String sorumluPersonel) {
            // Üst sınıfın (Departman) yapıcı metoduna adını ve sorumlusunu gönderiyoruz
            super("İnsan Kaynakları", sorumluPersonel);
        }

        // ----------------------------------------------------
        // İNSAN KAYNAKLARINA ÖZEL İŞLEMLER
        // ----------------------------------------------------
        public void iseAlimYap(String personelAdi) {
            islemAnimasyonu();
            calisanSayisi += 1; // Çalışan sayısını artır
            System.out.println("Tebrikler! " + personelAdi + " fabrikamızda işe başladı.");
        }

        public void bordroHazirla() {
            islemAnimasyonu();
            System.out.println("Maaşlar hesaplandı. Toplam " + calisanSayisi + " personele ödeme yapıldı.");
        }

        @Override
        public void ozelIslem() {
            System.out.println("İnsan Kaynakları özel menüsüne giriliyor...");
            // Burada eğitim planlama gibi İK'ya özel başka şeyler çağrılabilir
        }
    }

