package com.factory.stitch.service;

import com.factory.stitch.Main;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Malzeme Yönetimi Servis Sınıfı
 * Malzeme ekleme, stok güncelleme ve kritik stok kontrolü işlemlerini kapsar.
 *
 * NOT: Bu sınıf şu anda in-memory çalışmaktadır.
 *      Gerçek uygulamada `malzeme` ve `stok_hareket` tabloları kullanılmalıdır.
 */
public class MalzemeServisi {

    private final List<Malzeme> malzemeler = new ArrayList<>();

    public MalzemeServisi() {
        // Varsayılan örnek veriler (schema_mysql.sql'deki INSERT ile uyumlu)
        malzemeler.add(new Malzeme(1, "Demir Profil", 1200, 300, "ADET"));
        malzemeler.add(new Malzeme(2, "Çelik Vida", 5000, 1000, "ADET"));
        malzemeler.add(new Malzeme(3, "Bakır Kablo", 800, 250, "METRE"));
        malzemeler.add(new Malzeme(4, "Alüminyum Alaşım A-202", 12, 50, "KG"));
        malzemeler.add(new Malzeme(5, "Hassas Valf X-1", 8, 20, "ADET"));
    }

    /**
     * Yeni malzeme ekler.
     *
     * @param adi          Malzeme adı
     * @param stokMiktari  Başlangıç stok miktarı
     * @param kritikLimit  Kritik stok sınırı
     * @param birim        Ölçü birimi (ADET, KG, METRE vb.)
     */
    public void malzemeEkle(String adi, int stokMiktari, int kritikLimit, String birim) {
        if (adi == null || adi.isBlank()) {
            System.err.println("[HATA] Malzeme adı boş olamaz.");
            return;
        }
        if (stokMiktari < 0 || kritikLimit < 0) {
            System.err.println("[HATA] Stok miktarı ve kritik limit negatif olamaz.");
            return;
        }
        int yeniId = malzemeler.stream().mapToInt(m -> m.id).max().orElse(0) + 1;
        Malzeme yeni = new Malzeme(yeniId, adi, stokMiktari, kritikLimit, birim);
        malzemeler.add(yeni);
        System.out.println("[OK] Malzeme eklendi: " + yeni);
    }

    /**
     * Belirtilen miktarda stok günceller (giriş veya çıkış).
     *
     * @param malzemeId Güncellenecek malzeme kimliği
     * @param miktar    Değişim miktarı (pozitif=giriş, negatif=çıkış)
     */
    public void stokGuncelle(int malzemeId, int miktar) {
        Optional<Malzeme> bulunan = malzemeler.stream()
                .filter(m -> m.id == malzemeId)
                .findFirst();
        if (bulunan.isEmpty()) {
            System.err.println("[HATA] Malzeme bulunamadı: ID=" + malzemeId);
            return;
        }
        Malzeme m = bulunan.get();
        int yeniMiktar = m.stokMiktari + miktar;
        if (yeniMiktar < 0) {
            System.err.println("[HATA] Yetersiz stok. Mevcut: " + m.stokMiktari + " " + m.birim);
            return;
        }
        m.stokMiktari = yeniMiktar;
        System.out.printf("[OK] Stok güncellendi: %s → %d %s%n", m.adi, m.stokMiktari, m.birim);
        if (m.stokMiktari <= m.kritikLimit) {
            System.out.printf("[UYARI] KRİTİK STOK: %s (%d %s / Min: %d)%n",
                    m.adi, m.stokMiktari, m.birim, m.kritikLimit);
        }
    }

    /**
     * Kritik stok altındaki tüm malzemeleri listeler.
     */
    public void kritikStokRaporu() {
        System.out.println("\n=== Kritik Stok Raporu ===");
        malzemeler.stream()
                .filter(m -> m.stokMiktari <= m.kritikLimit)
                .forEach(m -> System.out.printf("  ⚠ %s: %d %s (Kritik Limit: %d)%n",
                        m.adi, m.stokMiktari, m.birim, m.kritikLimit));
    }

    /**
     * Tüm malzemeleri listeler.
     */
    public void malzemeleriListele() {
        System.out.println("\n=== Malzeme Listesi ===");
        malzemeler.forEach(System.out::println);
    }

    /**
     * Konsol tabanlı basit menü.
     */
    public void menuGoster() {
        System.out.println("\n--- Malzeme Yönetimi Modülü ---");
        int secim = Main.sayiOku("1 - Malzemeleri Listele\n2 - Kritik Stok Raporu\n3 - Stok Güncelle\n0 - Geri\nSeçiminiz: ");
        switch (secim) {
            case 1:
                malzemeleriListele();
                break;
            case 2:
                kritikStokRaporu();
                break;  
            case 3:
                stokGuncelle(4, -5); // Örnek: Alüminyum -5 kg
                break;
            default:
                System.out.println("Geri dönülüyor...");
        }
    }

    // ─── İç Veri Modeli ─────────────────────────────────────────────────

    static class Malzeme {
        int id, stokMiktari, kritikLimit;
        String adi, birim;

        Malzeme(int id, String adi, int stokMiktari, int kritikLimit, String birim) {
            this.id = id;
            this.adi = adi;
            this.stokMiktari = stokMiktari;
            this.kritikLimit = kritikLimit;
            this.birim = birim;
        }

        @Override
        public String toString() {
            String durum = stokMiktari <= kritikLimit ? " ⚠ KRİTİK" : "";
            return String.format("Malzeme{id=%d, adi='%s', stok=%d, limit=%d, birim='%s'}%s",
                    id, adi, stokMiktari, kritikLimit, birim, durum);
        }
    }
}
