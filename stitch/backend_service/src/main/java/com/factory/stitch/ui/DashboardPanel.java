package com.factory.stitch.ui;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;

/**
 * Tüm departman panelleri için temel (abstract) panel sınıfı.
 *
 * Her departman paneli bu sınıfı genişletmeli ve {@link #veriYenile()}
 * metodunu kendi verilerine göre uygulamalıdır.
 *
 * GÜVENLİK: Bu panel sınıfı kullanıcı adı / şifre içermez.
 * Kimlik doğrulama MainFrame üzerinden yapılmalı; bu sınıfa
 * yalnızca oturum açmış kullanıcının kimliği (rol, id) geçirilmelidir.
 */
public abstract class DashboardPanel extends JPanel {

    /** Bu panelin başlığı (sekme veya pencere başlığı için kullanılır). */
    private final String baslik;

    /**
     * @param baslik Panel başlığı (örn. "Satın Alma Paneli")
     */
    protected DashboardPanel(String baslik) {
        this.baslik = baslik;
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(12, 12, 12, 12));
        panelOlustur();
    }

    /**
     * Alt sınıf bu metodu override ederek kendi bileşenlerini ekler.
     * Constructor'dan çağrılır.
     */
    protected abstract void panelOlustur();

    /**
     * Panelin verilerini (servis katmanından) yeniden yükler.
     * Sekme seçildiğinde veya kullanıcı "Yenile" düğmesine bastığında çağrılır.
     */
    public abstract void veriYenile();

    /** @return Panel başlığı */
    public String getBaslik() {
        return baslik;
    }
}
