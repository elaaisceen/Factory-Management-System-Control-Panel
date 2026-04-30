package com.factory.stitch;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.client.RestTemplate;

import java.awt.Desktop;
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;

public class DesktopWebViewLauncher extends Application {

    private static final String API_BASE = "http://localhost:8080";
    private static final RestTemplate REST_TEMPLATE = new RestTemplate();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static ConfigurableApplicationContext context;

    private Label statusLabel;
    private WebEngine webEngine;
    private WebView webView;
    private String currentUrl;

    public static void setApplicationContext(ConfigurableApplicationContext applicationContext) {
        context = applicationContext;
    }

    @Override
    public void start(Stage stage) {
        // JavaFX Network izinleri ve User-Agent ayarları
        System.setProperty("javafx.web.allowInsecureContent", "true");
        System.setProperty("javafx.web.allowMixedContent", "true");
        
        stage.setTitle("Factory ERP Desktop Launcher v1.0");
        stage.setMinWidth(1200);
        stage.setMinHeight(800);

        Map<String, String> dashboards = new LinkedHashMap<>();
        dashboards.put("Anasayfa", "/index.html");
        dashboards.put("Giris", "/login_register/loginRegister.html");
        dashboards.put("Admin Dashboard", "/login_register/admin_dashboard/adminDashboard.html");
        dashboards.put("HR Dashboard", "/hr_dashboard/hr.html");
        dashboards.put("Production Dashboard", "/production_dashboard/production.html");
        dashboards.put("Stock Dashboard", "/stock_dashboard/stock.html");
        dashboards.put("Purchasing Dashboard", "/purchasing_dashboard/purchasing.html");
        dashboards.put("IT Dashboard", "/it_dashboard/it.html");
        dashboards.put("Finance Dashboard", "/finance_dashboard/finance.html");
        dashboards.put("Payroll Dashboard", "/finance_dashboard/maas.html");

        ComboBox<String> dashboardSelector = new ComboBox<>();
        dashboardSelector.getItems().addAll(dashboards.keySet());
        dashboardSelector.getSelectionModel().select("Anasayfa");

        Button openButton = new Button("Ac");
        Button openBrowserButton = new Button("Tarayicida Ac");
        Button refreshStatusButton = new Button("API Yenile");
        statusLabel = new Label("Backend durumu: kontrol ediliyor...");

        HBox topBar = new HBox(10, dashboardSelector, openButton, openBrowserButton, refreshStatusButton, statusLabel);
        topBar.setPadding(new Insets(10));

        webView = new WebView();
        webView.setZoom(0.9);

        webEngine = webView.getEngine();
        webEngine.setJavaScriptEnabled(true);
        
        // User-Agent ayarı
        webEngine.setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36 JavaFX/17");
        
        // Network izinleri
        webEngine.setUserDataDirectory(null);
        webEngine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                if (webEngine.getLocation() != null && webEngine.getLocation().contains("/login_register/loginRegister.html")) {
                    try {
                        webEngine.executeScript("localStorage.removeItem('oturum'); sessionStorage.clear();");
                    } catch (Exception ignored) {
                    }
                }
                injectDesktopUiFixes();
                injectJavaBridge();
            }
        });

        openButton.setOnAction(event -> {
            String key = dashboardSelector.getSelectionModel().getSelectedItem();
            if (key != null) {
                currentUrl = dashboards.get(key);
                // Load from classpath (static resources)
                try {
                    String resourceUrl = API_BASE + currentUrl;
                    webEngine.load(resourceUrl);
                } catch (Exception e) {
                    // Fallback to localhost
                    webEngine.load(API_BASE + currentUrl);
                }
            }
        });

        openBrowserButton.setOnAction(event -> openInSystemBrowser());
        refreshStatusButton.setOnAction(event -> refreshBackendStatus());

        BorderPane root = new BorderPane();
        root.setTop(topBar);
        root.setCenter(webView);

        stage.setScene(new Scene(root, 1600, 1000));
        stage.setMaximized(true);
        stage.show();

        openButton.fire();
        refreshBackendStatus();
    }

    private void openInSystemBrowser() {
        String url = currentUrl != null ? currentUrl : API_BASE + "/";
        
        // URL'yi düzelt - eğer relative ise tam URL yap
        if (!url.startsWith("http")) {
            url = API_BASE + url;
        }
        
        System.out.println("Opening in browser: " + url);
        System.out.println("Current URL: " + currentUrl);
        System.out.println("API_BASE: " + API_BASE);

        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(URI.create(url));
                System.out.println("Browser opened successfully using Desktop API");
                return;
            }
        } catch (Exception e) {
            System.out.println("Desktop API failed: " + e.getMessage());
        }

        try {
            ProcessBuilder pb = new ProcessBuilder("rundll32", "url.dll,FileProtocolHandler", url);
            pb.redirectErrorStream(true);
            Process process = pb.start();
            System.out.println("Browser opened using rundll32");
        } catch (Exception e) {
            System.out.println("rundll32 failed: " + e.getMessage());
            // Last resort: try PowerShell
            try {
                ProcessBuilder pb = new ProcessBuilder("powershell", "-Command", "Start-Process", url);
                pb.redirectErrorStream(true);
                Process process = pb.start();
                System.out.println("Browser opened using PowerShell");
            } catch (Exception ex) {
                System.out.println("PowerShell also failed: " + ex.getMessage());
                // Final fallback: try cmd.exe
                try {
                    ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "start", url);
                    pb.redirectErrorStream(true);
                    Process process = pb.start();
                    System.out.println("Browser opened using cmd.exe");
                } catch (Exception exc) {
                    System.out.println("All methods failed: " + exc.getMessage());
                }
            }
        }
    }

    private void injectJavaBridge() {
        try {
            JSObject window = (JSObject) webEngine.executeScript("window");
            window.setMember("javaBridge", new DashboardBridgeApi());
        } catch (Exception ignored) {
            // WebView bridge eklenemese de web dashboard HTTP uzerinden calismaya devam eder.
        }
    }

    private void injectDesktopUiFixes() {
    try {
        webEngine.executeScript("""
            (function applyDesktopUiFixes() {
              try {
                const doc = document;
                if (!doc || !doc.head) return;

                // Önce mevcut fix'leri temizle
                const existingFix = doc.getElementById("desktop-ui-fix");
                if (existingFix) existingFix.remove();
                const existingTextFix = doc.getElementById("desktop-text-fix");
                if (existingTextFix) existingTextFix.remove();
                const existingMinimal = doc.getElementById("desktop-minimal-fix");
                if (existingMinimal) existingMinimal.remove();

                // JavaFX WebView için ikonları gizle
                const iconStyle = doc.createElement('style');
                iconStyle.id = 'desktop-icon-fix';
                iconStyle.textContent = `
                    .material-symbols-outlined {
                        display: none !important;
                    }
                `;
                doc.head.appendChild(iconStyle);

                // Tüm ikonları zorla yeniden render et
                doc.querySelectorAll('.material-symbols-outlined').forEach(el => {
                    const text = el.textContent.trim();
                    el.textContent = '';
                    el.offsetHeight;
                    el.textContent = text;
                });

                // Minimal text fix
                const minimalTextStyle = doc.createElement('style');
                minimalTextStyle.id = 'desktop-minimal-fix';
                minimalTextStyle.textContent = `
                    html {
                        -webkit-text-size-adjust: 100%;
                        text-size-adjust: 100%;
                    }
                    body {
                        -webkit-text-size-adjust: 100%;
                        text-size-adjust: 100%;
                    }
                    * {
                        font-synthesis: none;
                    }
                `;
                doc.head.appendChild(minimalTextStyle);

              } catch (e) {
                console.error('Desktop UI fix error:', e);
              }
            })();
        """);
    } catch (Exception ignored) {
        // CSS enjekte edilemezse sayfa varsayilan haliyle devam eder.
    }
}
//     private void injectDesktopUiFixes() {
//         try {
//             webEngine.executeScript("""
//                     (function applyDesktopUiFixes() {
//                       try {
//                         const doc = document;
//                         if (!doc || !doc.head) {
//                           return;
//                         }

//                         // Önce mevcut desktop-ui-fix'i temizle
//                         const existingFix = doc.getElementById("desktop-ui-fix");
//                         if (existingFix) {
//                           existingFix.remove();
//                         }
//                         const existingTextFix = doc.getElementById("desktop-text-fix");
//                         if (existingTextFix) {
//                           existingTextFix.remove();
//                         }

//                         // Direkt style inject et - link tag yerine
//                         const fontStyle = doc.createElement('style');
//                         fontStyle.textContent = `
//                         @font-face {
//                             font-family: 'Material Symbols Outlined';
//                             font-style: normal;
//                             font-weight: 100 700;
//                             src: url('http://localhost:8080/assets/fonts/MaterialSymbols.woff2') format('woff2');
//                         }
//                         .material-symbols-outlined {
//                             font-family: 'Material Symbols Outlined' !important;
//                             font-weight: normal;
//                             font-style: normal;
//                             font-size: 24px;
//                             line-height: 1;
//                             letter-spacing: normal;
//                             text-transform: none;
//                             display: inline-block;
//                             white-space: nowrap;
//                             direction: ltr;
//                             -webkit-font-feature-settings: 'liga';
//                             font-feature-settings: 'liga';
//                             -webkit-font-smoothing: antialiased;
//                         }
//                         `;
// doc.head.appendChild(fontStyle);

//                         const manropeLink = doc.createElement('link');
//                         manropeLink.rel = 'stylesheet';
//                         manropeLink.href = '/assets/fonts/manrope.css';
//                         doc.head.appendChild(manropeLink);

//                         const interLink = doc.createElement('link');
//                         interLink.rel = 'stylesheet';
//                         interLink.href = '/assets/fonts/inter.css';
//                         doc.head.appendChild(interLink);

//                         // Font yüklendiğinde icon'ları yenile
//                         materialIconsLink.onload = function() {
//                           setTimeout(() => {
//                             const icons = doc.querySelectorAll('.material-symbols-outlined');
//                             icons.forEach(icon => {
//                               // Icon'u yeniden oluştur
//                               const parent = icon.parentNode;
//                               const newIcon = doc.createElement('span');
//                               newIcon.className = 'material-symbols-outlined';
//                               newIcon.textContent = icon.textContent;
//                               newIcon.style.fontFamily = 'Material Symbols Outlined';
//                               if (parent) {
//                                 parent.replaceChild(newIcon, icon);
//                               }
//                             });
//                           }, 100);
//                         };

//                         // Sadece minimal text size ayarları
//                         const minimalTextCSS = `
//                           html {
//                             -webkit-text-size-adjust: 100%;
//                             text-size-adjust: 100%;
//                           }
//                           body {
//                             -webkit-text-size-adjust: 100%;
//                             text-size-adjust: 100%;
//                           }
//                           * {
//                             font-synthesis: none;
//                           }
//                         `;

//                         const minimalTextStyle = doc.createElement("style");
//                         minimalTextStyle.id = "desktop-minimal-fix";
//                         minimalTextStyle.textContent = minimalTextCSS;
//                         doc.head.appendChild(minimalTextStyle);

//                       } catch (e) {
//                         console.error('Desktop UI fix error:', e);
//                       }
//                     })();
//                     """);
//         } catch (Exception ignored) {
//             // CSS enjekte edilemezse sayfa varsayilan haliyle devam eder.
//         }
//     }

    private void refreshBackendStatus() {
        new Thread(() -> {
            try {
                Map<?, ?> result = REST_TEMPLATE.getForObject(API_BASE + "/api/erp/health", Map.class);
                boolean success = result != null && Boolean.TRUE.equals(result.get("success"));
                Platform.runLater(() -> statusLabel.setText(success ? "Backend durumu: ACIK" : "Backend durumu: KAPALI"));
            } catch (Exception ex) {
                Platform.runLater(() -> statusLabel.setText("Backend durumu: KAPALI"));
            }
        }, "backend-health-check").start();
    }

    @Override
    public void stop() {
        ConfigurableApplicationContext activeContext = context != null ? context : DesktopLauncherMain.getContext();
        if (activeContext != null) {
            activeContext.close();
        }
        Platform.exit();
    }

    public static class DashboardBridgeApi {
        public String getDashboardSummary() {
            try {
                Object result = REST_TEMPLATE.getForObject(API_BASE + "/api/dashboard/summary", Object.class);
                return OBJECT_MAPPER.writeValueAsString(result);
            } catch (Exception e) {
                return "{\"success\":false,\"message\":\"Bridge summary unavailable.\"}";
            }
        }
    }
}
