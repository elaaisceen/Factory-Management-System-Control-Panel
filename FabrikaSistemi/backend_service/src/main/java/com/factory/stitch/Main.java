package com.factory.stitch;

// Gerekli modelleri ve yardımcı araçları içeri aktarıyoruz

import com.factory.stitch.model.Department;
import com.factory.stitch.model.Depo;
import com.factory.stitch.model.Finans;
import com.factory.stitch.model.HumanResources;
import com.factory.stitch.model.It;
import com.factory.stitch.model.SatinAlma;
import com.factory.stitch.model.SurecYurutulebilir;
import com.factory.stitch.model.Uretim;
import com.factory.stitch.util.DosyaKayitUtil;
import com.factory.stitch.config.DatabaseConnection;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

// NOT: Bu Scanner tüm uygulama boyunca tek bir System.in akışını paylaşır.
// Her metod çağrısında yeni Scanner açmak System.in'i kapatır ve sonraki
// okumalar başarısız olur. Bu nedenle paylaşımlı static scanner kullanılır.

@SpringBootApplication
public class Main {

    private static final DateTimeFormatter ZAMAN_FORMATI = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    public CommandLineRunner runConsoleApp() {
        return args -> {
            try {
                System.out.println("=== Fabrika ERP Spring Boot Backend Baslatildi ===");
                DatabaseConnection.getConnection();
                System.out.println("Baglanti Basarili!");
            } catch (Exception e) {
                System.out.println("Veritabani uyarisi: " + e.getMessage());
            }
            // Not: Spring Boot REST API olarak calisacagi icin Scanner loop'u devre disi birakildi.
        };
    }


    // Seçilen departmana özel işlemleri yöneten alt menü.

    private static void departmanMenu(Scanner input, Department departman) {
        boolean menuAcik = true;
        while (menuAcik) {
            System.out.println("\n--- " + departman.getDepartmanAdi() + " Menusu ---");
            System.out.println("Sorumlu: " + departman.getSorumluPersonel());
            System.out.println("1. Durum Goster");
            System.out.println("2. Surec Simulasyonu Calistir");
            System.out.println("3. Dosyaya Islem Kaydi Yaz");
            System.out.println("0. Ust Menuye Don");
            System.out.print("Secim: ");

            int altSecim = sayiOku("Lutfen bir secim yapiniz: ");
            switch (altSecim) {
                case 1:
                    // Override edilmiş metod çağrılır.
                    departman.durumGoster();
                    break;
                case 2:
                    /*
                     * Interface Kontrolü (instanceof):
                     * Her departman bir süreç yürütmeyebilir. 
                     * Burada "Bu departman SurecYurutulebilir yeteneğine sahip mi?" diye bakıyoruz.
                     */
                    if (departman instanceof SurecYurutulebilir) {
                        // Downcasting: Departmanı interface tipine dönüştürüp metodunu çalıştırıyoruz.
                        ((SurecYurutulebilir) departman).departmanSureciniYurut();
                        // Log formatını hazırlayıp DosyaKayitUtil üzerinden dosyaya yazıyoruz.
                        String satir = String.format("[%s] %s departmani sureci %s tarafindan calistirildi.",
                                LocalDateTime.now().format(ZAMAN_FORMATI),
                                departman.getDepartmanAdi(),
                                departman.getSorumluPersonel());
                        DosyaKayitUtil.logSatiriEkle(satir);
                        System.out.println("Surec tamamlandi ve log dosyasina kaydedildi.");
                    } else {
                        System.out.println("Bu departman icin surec arayuzu tanimli degil.");
                    }
                    break;
                case 3:
                    // Kullanıcı tıkladığında manuel bir işlem kaydı oluşturur.
                    String manuelSatir = String.format("[%s] Manuel log: %s departmani goruntulendi.",
                            LocalDateTime.now().format(ZAMAN_FORMATI),
                            departman.getDepartmanAdi());
                    DosyaKayitUtil.logSatiriEkle(manuelSatir);
                    System.out.println("Kayit olusturuldu: " + DosyaKayitUtil.logDosyaYolu());
                    break;
                case 0:
                    menuAcik = false;
                    break;
                default:
                    System.out.println("Gecersiz islem secimi.");
            }
        }
    }

    /**
     * Input validation (Giriş Doğrulama):
     * Kullanıcı sayı yerine harf girerse programın çökmesini engeller.
     *
     * Paylaşımlı static Scanner kullanılır: her çağrıda yeni Scanner(System.in)
     * açmak, önceki Scanner kapandığında System.in'i de kapattığından sonraki
     * okumalar NoSuchElementException fırlatır. Bu yöntem bu sorunu önler.
     *
     * @param mesaj Kullanıcıya gösterilecek istem metni
     * @return Kullanıcının girdiği geçerli tam sayı
     */
    private static final Scanner KONSOL_OKUYUCU = new Scanner(System.in);

    public static int sayiOku(String mesaj) {
        System.out.print(mesaj);
        while (!KONSOL_OKUYUCU.hasNextInt()) {
            KONSOL_OKUYUCU.nextLine(); // Geçersiz satırı temizle
            System.out.print("Lutfen sayisal bir secim girin: ");
        }
        int sonuc = KONSOL_OKUYUCU.nextInt();
        KONSOL_OKUYUCU.nextLine(); // Satır sonunu temizle
        return sonuc;
    }
}
