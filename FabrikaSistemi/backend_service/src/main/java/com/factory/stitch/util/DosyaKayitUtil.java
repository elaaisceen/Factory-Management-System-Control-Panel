package com.factory.stitch.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * Uygulama genelindeki işlem kayıtlarını (log) dosyaya yazmak için kullanılan
 * yardımcı sınıf.
 * Final olarak tanımlanmıştır çünkü kalıtım yoluyla genişletilmesi
 * amaçlanmamıştır.
 *
 * <p>
 * <b>Sorumluluk sınırı:</b> Bu sınıf yalnızca dosya yazma işlemleri yapar.
 * Kullanıcıdan giriş okuma (<code>sayiOku</code>) bu sınıfın görevi değildir;
 * bu işlev {@link com.factory.stitch.Main#sayiOku(String)} metodunda tanımlıdır
 * ve tüm servis sınıfları (SatinAlmaService, MalzemeServisi vb.) onu oradan
 * kullanır.
 * </p>
 */

public final class DosyaKayitUtil {
    // Kayıtların tutulacağı varsayılan dosya yolu: projenin ana dizinindeki "logs"
    // klasörü içindedir.
    private static final Path LOG_DOSYASI = Path.of("logs", "islem-kaydi.txt");

    /**
     * Private constructor tercih edilmiştir çünkü bu sınıfın dışarıdan "new"
     * anahtar kelimesi ile örneğinin oluşturulmasını engellemek istenmiştir.
     */

    private DosyaKayitUtil() {
    }

    /**
     * Verilen metni log dosyasına yeni bir satır olarak ekler.
     * Eğer klasör veya dosya yoksa otomatik olarak oluşturur.
     * * @param satir Dosyaya yazılacak olan metin içeriğidir.
     */

    public static void logSatiriEkle(String satir) {
        try {
            // Log dosyasının içinde bulunacağı klasörleri (logs/) oluşturur.
            Files.createDirectories(LOG_DOSYASI.getParent());
            // Dosyaya metni yazar
            Files.writeString(
                    LOG_DOSYASI,
                    satir + System.lineSeparator(), // İşletim sistemine uygun alt satıra geçme karakteri ekler
                    StandardCharsets.UTF_8, // Karakter kodlamasını UTF-8 olarak belirler
                    StandardOpenOption.CREATE, // Dosya yoksa oluştur
                    StandardOpenOption.APPEND // Dosya varsa sonuna ekle (üzerine yazmaz)
            );
        } catch (IOException e) {
            // Hata durumunda kullanıcıyı bilgilendirir ancak uygulamayı durdurmaz
            System.out.println("[UYARI] İşlem dosyaya yazılamadı: " + e.getMessage());
        }
    }

    /**
     * Log dosyasının sistemdeki tam yolunu döndürür.
     * * @return Dosyanın tam yoludur.
     */

    public static String logDosyaYolu() {
        return LOG_DOSYASI.toAbsolutePath().toString();
    }
}
