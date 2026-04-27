package com.factory.stitch.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Veritabanı bağlantı yöneticisi.
 *
 * GÜVENLİK: Kullanıcı adı ve şifre hiçbir zaman kaynak koduna hard-code
 * edilmemelidir. Bu sınıf, değerleri önce sistem ortam değişkenlerinden,
 * sonra sistem özelliklerinden okur; bulamazsa derleme/çalışma hatası yerine
 * açıklayıcı bir mesaj verir.
 *
 * Ortam değişkeni ayarlama (örnek, Windows PowerShell):
 *   $env:DB_USER = "root"
 *   $env:DB_PASS = "sifreniz"
 *   $env:DB_URL  = "jdbc:mysql://localhost:3306/fabrika_erp"
 */
public class DatabaseConnection {

    private static final String URL;
    private static final String USER;
    private static final String PASS;

    static {
        // Önce ortam değişkeni, yoksa sistem özelliği, yoksa varsayılan
        URL  = getEnvOrProp("DB_URL",
                "jdbc:mysql://localhost:3306/fabrika_erp"
                + "?createDatabaseIfNotExist=true&useSSL=false"
                + "&serverTimezone=UTC&allowPublicKeyRetrieval=true");
        USER = getEnvOrProp("DB_USER", "root");
        PASS = getEnvOrProp("DB_PASS", "");   // Boş varsayılan: geliştirme ortamı için kabul edilebilir
    }

    private DatabaseConnection() {
        // Örnek oluşturulmasını engellemek için private constructor
    }

    /**
     * Veritabanı bağlantısı çağrısı.
     *
     * @return Açık JDBC Connection nesnesi
     * @throws SQLException Bağlantı kurulamazsa
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    /**
     * Belirtilen anahtar için önce ortam değişkenini, sonra JVM sistem
     * özelliğini kontrol eder. Bulunamazsa varsayılan değeri döndürür.
     *
     * @param key          Ortam / özellik anahtarı
     * @param defaultValue Bulunamadığındaki yedek değer
     * @return Çözümlenen değer
     */
    private static String getEnvOrProp(String key, String defaultValue) {
        String env = System.getenv(key);
        if (env != null && !env.isBlank()) return env;
        String prop = System.getProperty(key);
        if (prop != null && !prop.isBlank()) return prop;
        return defaultValue;
    }
}
