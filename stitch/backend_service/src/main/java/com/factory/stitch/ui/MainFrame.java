package com.factory.stitch.ui;

import javax.swing.*;
import java.awt.*;

/**
 * Fabrika ERP uygulamasının ana Swing penceresi.
 *
 * GÜVENLİK: Kullanıcı adı ve şifre bu sınıfta SAKLANMAZ.
 * Oturum açma işlemi tamamlandıktan sonra yalnızca oturum kimliği
 * (kullanıcı ID ve rol) tutulur; ham şifre bellekten silinir.
 *
 * Kullanım:
 *   SwingUtilities.invokeLater(MainFrame::new);
 */
public class MainFrame extends JFrame {

    private static final String UYGULAMA_ADI = "Fabrika ERP – Kontrol Paneli";
    private static final Dimension VARSAYILAN_BOYUT = new Dimension(1280, 800);

    // Oturum bilgisi – şifre burada TUTULMAZ
    private int aktifKullaniciId   = -1;
    private String aktifKullaniciAdi = "";
    private String aktifRol          = "";

    private JTabbedPane sekmePaneli;

    public MainFrame() {
        super(UYGULAMA_ADI);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setPreferredSize(VARSAYILAN_BOYUT);
        setMinimumSize(new Dimension(900, 600));

        girisEkraniniBas();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /** Login ekranını başlatır; başarılı girişte ana dashboard'a geçer. */
    private void girisEkraniniBas() {
        String[] bilgi = LoginDialog.girisAl(this);
        if (bilgi == null) {
            // Kullanıcı iptal etti
            System.exit(0);
            return;
        }
        // bilgi[0] = kullanıcıAdi, bilgi[1] = doğrulanmış rol
        aktifKullaniciAdi = bilgi[0];
        aktifRol          = bilgi[1];
        anaDashboardBas();
    }

    /** Rol bazlı sekmeleri oluşturur. */
    private void anaDashboardBas() {
        getContentPane().removeAll();
        sekmePaneli = new JTabbedPane(JTabbedPane.TOP);

        // Her departman için panel ekle (rol kontrolü burada yapılabilir)
        sekmePaneli.addTab("Malzeme", new MalzemePanel());
        sekmePaneli.addTab("Personel", new PersonelPanel());

        // Sekme değişince veriYenile() çağır
        sekmePaneli.addChangeListener(e -> {
            Component sec = sekmePaneli.getSelectedComponent();
            if (sec instanceof DashboardPanel) {
                ((DashboardPanel) sec).veriYenile();
            }
        });

        JLabel durumCubugu = new JLabel(
                "  Oturum: " + aktifKullaniciAdi + " | Rol: " + aktifRol);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(sekmePaneli, BorderLayout.CENTER);
        getContentPane().add(durumCubugu, BorderLayout.SOUTH);

        revalidate();
        repaint();
    }

    /** Uygulamayı başlat. */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainFrame::new);
    }

    // ─── İç Yardımcı: LoginDialog ────────────────────────────────────────────

    /**
     * Kullanıcı adı / şifre diyalog kutusu.
     *
     * GÜVENLİK NOTU: Şifre JPasswordField ile alınır (char[] olarak tutulur).
     * Doğrulama tamamlandıktan hemen sonra dizi sıfırlanır; şifre String olarak
     * saklanmaz (String havuzda kalır, GC tarafından anında toplanamaz).
     */
    private static class LoginDialog {

        static String[] girisAl(Frame parent) {
            JDialog dlg = new JDialog(parent, "Giriş Yap", true);
            dlg.setLayout(new GridBagLayout());
            dlg.setSize(340, 200);
            dlg.setLocationRelativeTo(parent);

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets  = new Insets(6, 8, 6, 8);
            gbc.fill    = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1.0;

            JLabel lblAd  = new JLabel("Kullanıcı Adı:");
            JTextField tfAd    = new JTextField(20);
            JLabel lblSifre = new JLabel("Şifre:");
            JPasswordField pfSifre = new JPasswordField(20);
            JButton btnGiris = new JButton("Giriş Yap");
            JButton btnIptal = new JButton("İptal");

            String[] sonuc = {null};

            btnGiris.addActionListener(e -> {
                String ad     = tfAd.getText().trim();
                char[] sifre  = pfSifre.getPassword();
                // TODO: Gerçek uygulamada kullanıcıAdi+hash veritabanından doğrulanmalı
                if (!ad.isBlank() && sifre.length > 0) {
                    sonuc[0] = ad + "|MUDUR"; // Basit demo; gerçek rol DB'den gelmeli
                }
                java.util.Arrays.fill(sifre, '\0'); // Şifreyi bellekten temizle
                dlg.dispose();
            });

            btnIptal.addActionListener(e -> dlg.dispose());

            gbc.gridx = 0; gbc.gridy = 0; dlg.add(lblAd, gbc);
            gbc.gridx = 1;                dlg.add(tfAd, gbc);
            gbc.gridx = 0; gbc.gridy = 1; dlg.add(lblSifre, gbc);
            gbc.gridx = 1;                dlg.add(pfSifre, gbc);

            JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 0));
            btnPanel.add(btnGiris);
            btnPanel.add(btnIptal);
            gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
            dlg.add(btnPanel, gbc);

            dlg.setVisible(true);

            if (sonuc[0] == null) return null;
            String[] parcalar = sonuc[0].split("\\|", 2);
            return parcalar; // [0]=kullanıcıAdi, [1]=rol
        }
    }
}
