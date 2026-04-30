package com.factory.stitch.service;

import com.factory.stitch.Main;
import com.factory.stitch.model.SatinAlma;
import java.util.ArrayList;
import java.util.List;

/**
 * Satın Alma İşlemleri Servis Sınıfı
 * Tedarikçi ekleme, sipariş oluşturma ve talep yönetimi işlemlerini kapsar.
 *
 * NOT: Bu sınıf şu anda in-memory (bellekte) çalışmaktadır.
 *      Gerçek uygulamada veritabanı bağlantısı (schema_mysql.sql) ile entegre edilmelidir.
 */
public class SatinAlmaService {

    // Basit in-memory veri modelleri
    private final List<Tedarikci> tedarikciler = new ArrayList<>();
    private final List<SatinAlmaSiparis> siparisler = new ArrayList<>();

    /**
     * Yeni tedarikçi ekler.
     *
     * @param adi      Tedarikçi adı (benzersiz olmalı)
     * @param ulke     Tedarikçinin bulunduğu ülke
     * @param email    İletişim e-postası
     */
    public void tedarikciEkle(String adi, String ulke, String email) {
        if (adi == null || adi.isBlank()) {
            System.err.println("[HATA] Tedarikçi adı boş olamaz.");
            return;
        }
        boolean zatenVar = tedarikciler.stream()
                .anyMatch(t -> t.adi.equalsIgnoreCase(adi));
        if (zatenVar) {
            System.out.println("[UYARI] Bu isimde tedarikçi zaten mevcut: " + adi);
            return;
        }
        Tedarikci yeni = new Tedarikci(tedarikciler.size() + 1, adi, ulke, email);
        tedarikciler.add(yeni);
        System.out.println("[OK] Tedarikçi eklendi: " + yeni);
    }

    /**
     * Malzeme satın alma siparişi oluşturur.
     *
     * @param tedarikciId  Tedarikçi kimliği
     * @param malzemeAdi   Sipariş edilecek malzeme adı
     * @param miktar       Sipariş miktarı (pozitif tamsayı)
     * @param birimFiyat   Birim fiyat (pozitif ondalık)
     */
    public void malzemeAl(int tedarikciId, String malzemeAdi, int miktar, double birimFiyat) {
        if (miktar <= 0) {
            System.err.println("[HATA] Miktar sıfırdan büyük olmalıdır.");
            return;
        }
        if (birimFiyat < 0) {
            System.err.println("[HATA] Birim fiyat negatif olamaz.");
            return;
        }
        Tedarikci tedarikci = tedarikciler.stream()
                .filter(t -> t.id == tedarikciId)
                .findFirst()
                .orElse(null);
        if (tedarikci == null) {
            System.err.println("[HATA] Tedarikçi bulunamadı: ID=" + tedarikciId);
            return;
        }
        SatinAlmaSiparis siparis = new SatinAlmaSiparis(
                siparisler.size() + 1,
                tedarikci,
                malzemeAdi,
                miktar,
                birimFiyat
        );
        siparisler.add(siparis);
        System.out.println("[OK] Sipariş oluşturuldu: " + siparis);
    }

    /**
     * Tüm siparişleri listeler.
     */
    public void siparisleriListele() {
        if (siparisler.isEmpty()) {
            System.out.println("Henüz sipariş bulunmuyor.");
            return;
        }
        System.out.println("=== Satın Alma Siparişleri ===");
        siparisler.forEach(System.out::println);
    }

    /**
     * Konsol tabanlı interaktif menü.
     *
     * Kullanıcıdan gerçek veri alınır; hard-coded örnek değer kullanılmaz.
     */
    public void menuGoster() {
        System.out.println("\n--- Satın Alma Modülü ---");
        int secim = Main.sayiOku(
                "1 - Tedarikçi Ekle\n2 - Sipariş Oluştur\n3 - Siparişleri Listele\n0 - Geri\nSeçiminiz: ");
        switch (secim) {
            case 1:
                tedarikciEkleInteraktif();
                break;
            case 2:
                malzemeAlInteraktif();
                break;
            case 3:
                siparisleriListele();
                break;
            default:
                System.out.println("Geri dönülüyor...");
        }
    }

    /**
     * Kullanıcıdan tedarikçi bilgilerini okuyarak tedarikciEkle() çağırır.
     */
    private void tedarikciEkleInteraktif() {
        java.util.Scanner sc = new java.util.Scanner(System.in);
        System.out.print("Tedarikçi Adı  : ");
        String adi = sc.nextLine().trim();
        System.out.print("Ülke           : ");
        String ulke = sc.nextLine().trim();
        System.out.print("E-Posta        : ");
        String email = sc.nextLine().trim();
        tedarikciEkle(adi, ulke, email);
    }

    /**
     * Kullanıcıdan sipariş bilgilerini okuyarak malzemeAl() çağırır.
     */
    private void malzemeAlInteraktif() {
        int tedarikciId = Main.sayiOku("Tedarikçi ID   : ");
        java.util.Scanner sc = new java.util.Scanner(System.in);
        System.out.print("Malzeme Adı    : ");
        String malzemeAdi = sc.nextLine().trim();
        int miktar       = Main.sayiOku("Miktar (adet)  : ");
        System.out.print("Birim Fiyat    : ");
        double birimFiyat = 0;
        try {
            birimFiyat = Double.parseDouble(sc.nextLine().trim().replace(",", "."));
        } catch (NumberFormatException e) {
            System.err.println("[HATA] Geçersiz fiyat formatı.");
            return;
        }
        malzemeAl(tedarikciId, malzemeAdi, miktar, birimFiyat);
    }

    // ─── İç Veri Modelleri ───────────────────────────────────────────────

    static class Tedarikci {
        int id;
        String adi, ulke, email;

        Tedarikci(int id, String adi, String ulke, String email) {
            this.id = id; this.adi = adi; this.ulke = ulke; this.email = email;
        }

        @Override
        public String toString() {
            return String.format("Tedarikçi{id=%d, adi='%s', ulke='%s', email='%s'}", id, adi, ulke, email);
        }
    }

    static class SatinAlmaSiparis {
        int id, miktar;
        double birimFiyat;
        String malzemeAdi, durum;
        Tedarikci tedarikci;

        SatinAlmaSiparis(int id, Tedarikci tedarikci, String malzemeAdi, int miktar, double birimFiyat) {
            this.id = id;
            this.tedarikci = tedarikci;
            this.malzemeAdi = malzemeAdi;
            this.miktar = miktar;
            this.birimFiyat = birimFiyat;
            this.durum = "BEKLIYOR";
        }

        public double toplamTutar() {
            return miktar * birimFiyat;
        }

        @Override
        public String toString() {
            return String.format("Sipariş{id=%d, tedarikci='%s', malzeme='%s', miktar=%d, toplamTutar=%.2f TL, durum='%s'}",
                    id, tedarikci.adi, malzemeAdi, miktar, toplamTutar(), durum);
        }
    }
}
