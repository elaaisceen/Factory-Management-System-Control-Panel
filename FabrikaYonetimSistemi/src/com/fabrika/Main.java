package com.fabrika;

// Gerekli modelleri ve yardımcı araçları içeri aktarıyoruz

import com.fabrika.model.Department;
import com.fabrika.model.Depo;
import com.fabrika.model.Finans;
import com.fabrika.model.HumanResources;
import com.fabrika.model.It;
import com.fabrika.model.SatinAlma;
import com.fabrika.model.SurecYurutulebilir;
import com.fabrika.model.Uretim;
import com.fabrika.util.DosyaKayitUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    // Log kayıtlarında zamanı standart bir formatta (Gün.Ay.Yıl Saat:Dakika:Saniye) göstermek için sabit tanımladık.
    private static final DateTimeFormatter ZAMAN_FORMATI = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        Map<Integer, Department> rolMatrisi = new LinkedHashMap<>();
        rolMatrisi.put(1, new HumanResources("Ahmet Y."));
        rolMatrisi.put(2, new Uretim("Mehmet K."));
        rolMatrisi.put(3, new Depo("Ali Riza"));
        rolMatrisi.put(4, new It("Ela Eda"));
        rolMatrisi.put(5, new SatinAlma("Gizem S."));
        rolMatrisi.put(6, new Finans("Aylin T."));

        boolean sistemAcik = true;
        while (sistemAcik) {
            System.out.println("\n===== FABRIKA YONETIM SISTEMI =====");
            System.out.println("Rol seciniz:");
            System.out.println("1. IK | 2. Uretim | 3. Depo | 4. BT | 5. Satin Alma | 6. Finans | 0. Cikis");
            System.out.print("Secim: ");

            // Kullanıcıdan giriş alırken hata payını (harf girilmesi vb.) sayiOku metoduyla engelliyoruz.

            int secim = sayiOku(input);
            if (secim == 0) {
                sistemAcik = false;
                continue;
            }

            // Polymorphism (Çok Biçimlilik) burada devreye giriyor:
            // Hangi sınıf olursa olsun (IT, Depo vb.) hepsi bir 'Department'tır.

            Department departman = rolMatrisi.get(secim);
            if (departman == null) {
                System.out.println("Gecersiz rol secimi.");
                continue;
            }

            // Seçilen departmanın alt menüsüne yönlendiriyoruz.

            departmanMenu(input, departman);
        }

        input.close();
        System.out.println("Sistem kapatildi.");
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

            int altSecim = sayiOku(input);
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
     */

    private static int sayiOku(Scanner input) {
        while (!input.hasNextInt()) {
            input.nextLine(); // Hatalı girişi temizle
            System.out.print("Lutfen sayisal bir secim girin: ");
        }
        int sonuc = input.nextInt();
        input.nextLine(); // Buffer temizliği (Enter karakterini yutması için)
        return sonuc;
    }
}
