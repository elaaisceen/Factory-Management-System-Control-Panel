package com.factory.stitch.ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;

public class DesktopLauncher extends Application {

    private static final String APP_TITLE   = "PULSE INDUSTRIAL — Fabrika Yönetim Sistemi";
    private static final double WIN_W       = 1440;
    private static final double WIN_H       = 900;

    public static void launch(String[] args) {
        Application.launch(DesktopLauncher.class, args);
    }

    @Override
    public void start(Stage stage) {
        openMainWindow(stage);
    }

    private void openMainWindow(Stage stage) {
        // Standart DECORATED moduna geçiyoruz (Kapatma, küçültme butonları için)
        stage.initStyle(StageStyle.DECORATED);

        WebView webView = new WebView();
        WebEngine engine = webView.getEngine();
        webView.setContextMenuEnabled(false);

        String startPage = resolveStartPage();
        System.out.println("[DEBUG] Yüklenen Sayfa: " + startPage);

        StackPane root = new StackPane(webView);
        Scene scene = new Scene(root, WIN_W, WIN_H);

        stage.setScene(scene);
        stage.setTitle(APP_TITLE);
        stage.show();
        stage.centerOnScreen();

        engine.load(startPage);
    }

    private String resolveStartPage() {
        File currentDir = new File(System.getProperty("user.dir"));
        File stitchDir = currentDir.getName().equals("backend_service") ? currentDir.getParentFile() : currentDir;
        File loginFile = new File(stitchDir, "login_register/loginRegister.html");

        if (loginFile.exists()) {
            return loginFile.toURI().toString();
        }

        File altLoginFile = new File(currentDir, "../login_register/loginRegister.html");
        if (altLoginFile.exists()) {
            return altLoginFile.toURI().toString();
        }

        return "about:blank";
    }
}
