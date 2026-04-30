package com.factory.stitch.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Veritabanı bağlantı yöneticisi.
 */
public class DatabaseConnection {

    private static final String URL;
    private static final String USER;
    private static final String PASS;

    static {
        // Portu tekrar 3307'ye çektik
        URL  = getEnvOrProp("DB_URL",
                "jdbc:mysql://127.0.0.1:3307/factorylocal"
                + "?createDatabaseIfNotExist=true&useSSL=false"
                + "&serverTimezone=UTC&allowPublicKeyRetrieval=true");
        USER = getEnvOrProp("DB_USER", "root");
        PASS = getEnvOrProp("DB_PASS", "factory_26");
    }

    private DatabaseConnection() {}

    public static Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (SQLException e) {
            System.err.println("[HATA] Veritabanı Bağlantısı Başarısız! Port: 3306, Kullanıcı: " + USER);
            System.err.println("Hata Detayı: " + e.getMessage());
            throw e;
        }
    }

    private static String getEnvOrProp(String key, String defaultValue) {
        String env = System.getenv(key);
        if (env != null && !env.isBlank()) return env;
        String prop = System.getProperty(key);
        if (prop != null && !prop.isBlank()) return prop;
        return defaultValue;
    }
}
