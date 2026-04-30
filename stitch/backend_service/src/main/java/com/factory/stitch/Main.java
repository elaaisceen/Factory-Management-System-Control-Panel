package com.factory.stitch;

import com.factory.stitch.ui.DesktopLauncher;
import com.factory.stitch.config.DatabaseConnection;
import com.factory.stitch.model.Kullanici;
import com.factory.stitch.repository.KullaniciRepository;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

@SpringBootApplication
public class Main {

    private static final DateTimeFormatter ZAMAN_FORMATI =
            DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    private static ConfigurableApplicationContext springContext;
    private static final Scanner SCANNER = new Scanner(System.in);

    public static void main(String[] args) {
        Thread springThread = new Thread(() -> {
            try {
                springContext = SpringApplication.run(Main.class, args);
                System.out.println("[" + LocalDateTime.now().format(ZAMAN_FORMATI) + "] "
                        + "Backend REST API hazır (Port: 8080)");
            } catch (Exception e) {
                System.err.println("[KRITIK HATA] Spring Boot baslatilamadi: " + e.getMessage());
            }
        }, "spring-boot-thread");

        springThread.setDaemon(true);
        springThread.start();

        // JavaFX UI
        DesktopLauncher.launch(args);

        if (springContext != null) {
            springContext.close();
        }
        System.exit(0);
    }

    /**
     * Uygulama basladiginda varsayilan verileri hazirlar.
     */
    @Bean
    public CommandLineRunner setupData(KullaniciRepository repository) {
        return args -> {
            System.out.println("=== Sistem Verileri Kontrol Ediliyor ===");
            
            // Varsayilan Admin Hesabi (admin@fabrika.com / admin123)
            String adminEmail = "admin@fabrika.com";
            if (repository.findByKullaniciAdi(adminEmail).isEmpty()) {
                Kullanici admin = new Kullanici();
                admin.setAdSoyad("Sistem Yöneticisi");
                admin.setKullaniciAdi(adminEmail);
                admin.setSifreHash("admin123");
                admin.setRolId(2); // GELISTIRICI_ADMIN
                admin.setAktif(true);
                repository.save(admin);
                System.out.println("[OK] Varsayılan admin hesabı oluşturuldu: " + adminEmail);
            } else {
                System.out.println("[OK] Admin hesabı zaten mevcut.");
            }
            
            try {
                DatabaseConnection.getConnection();
                System.out.println("[OK] Veritabanı bağlantısı sağlıklı.");
            } catch (Exception e) {
                System.err.println("[UYARI] Veritabanı hatası: " + e.getMessage());
            }
        };
    }

    // --- Eski Servisler (Malzeme, SatinAlma vb.) İçin Yardımcı Metodlar ---
    public static int sayiOku(String mesaj) {
        System.out.print(mesaj);
        try {
            String input = SCANNER.nextLine();
            return Integer.parseInt(input);
        } catch (Exception e) {
            return -1;
        }
    }

    public static String metinOku(String mesaj) {
        System.out.print(mesaj);
        return SCANNER.nextLine();
    }
}
