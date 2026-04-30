package com.fabrika;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import java.io.File;

/**
 * Fabrika ERP - Masaüstü Uygulaması (WebView Versiyonu)
 * Bu sınıf, hazırladığınız HTML/JS arayüzlerini bir pencere içinde çalıştırır.
 */
public class Main extends Application {

    // HTML dosyalarının ana dizini
    private static final String STITCH_PATH = 
        "C:/Users/elanu/Desktop/FactoryManagementSystemControlPanel0/stitch/";

    @Override
    public void start(Stage primaryStage) {
        try {
            // JavaFX WebView bileşeni (Tarayıcı motoru)
            WebView webView = new WebView();
            WebEngine webEngine = webView.getEngine();

            // Sağ tık menüsünü ve JavaScript hatalarını yönetme (isteğe bağlı)
            webView.setContextMenuEnabled(true);
            
            // Başlangıç dosyası: index.html
            File file = new File(STITCH_PATH + "index.html");
            if (!file.exists()) {
                System.err.println("Hata: index.html bulunamadı! Yol: " + file.getAbsolutePath());
            } else {
                // Dosyayı yükle
                webEngine.load(file.toURI().toString());
            }

            // Layout
            BorderPane root = new BorderPane();
            root.setCenter(webView);

            // Sahne ve Pencere Ayarları
            Scene scene = new Scene(root, 1280, 800);
            primaryStage.setTitle("Pulse Industrial - Fabrika Yönetim & Takip Sistemi");
            
            // Uygulama ikonu ve pencere özellikleri
            primaryStage.setScene(scene);
            primaryStage.show();

            System.out.println("[INFO] Masaüstü uygulaması başlatıldı. HTML içerikleri yükleniyor...");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Uygulamayı başlat
        launch(args);
    }
}
