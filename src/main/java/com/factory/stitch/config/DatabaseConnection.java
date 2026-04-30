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
 *   $env:DB_URL  = "jdbc:mysql://localhost:3307/fabrika_erp"
 */

// public class DatabaseConnection {
//     private static final String URL = System.getenv().getOrDefault(
//             "DB_URL",
//             "jdbc:mysql://localhost:3307/fabrika_erp"
//             + "?createDatabaseIfNotExist=true&useSSL=false"
//             + "&serverTimezone=UTC&allowPublicKeyRetrieval=true"
//     );
//     private static final String USER = System.getenv().getOrDefault("DB_USERNAME", "root");
//     private static final String PASS = System.getenv().getOrDefault("DB_PASSWORD", "");

//     public static Connection getConnection() throws SQLException {
//         return DriverManager.getConnection(URL, USER, PASS);
//     }
// }

public class DatabaseConnection {

    private static final String URL;
    private static final String USER;
    private static final String PASS;

    static {
        // Spring Boot ile ayni anahtarlari kullan:
        // DB_URL / DB_USERNAME / DB_PASSWORD (opsiyonel legacy: DB_USER / DB_PASS)
        URL = getEnvOrProp("DB_URL",
                "jdbc:h2:mem:fabrikaerp;MODE=MySQL;DATABASE_TO_LOWER=TRUE;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");

        USER = firstNonBlank(
                getEnvOrPropNullable("DB_USERNAME"),
                getEnvOrPropNullable("DB_USER"),
                "sa"
        );

        PASS = firstNonBlank(
                getEnvOrPropNullable("DB_PASSWORD"),
                getEnvOrPropNullable("DB_PASS"),
                ""
        );
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

    private static String getEnvOrPropNullable(String key) {
        String env = System.getenv(key);
        if (env != null && !env.isBlank()) return env;
        String prop = System.getProperty(key);
        if (prop != null && !prop.isBlank()) return prop;
        return null;
    }

    private static String firstNonBlank(String... values) {
        for (String v : values) {
            if (v != null && !v.isBlank()) return v;
        }
        return "";
    }
}
