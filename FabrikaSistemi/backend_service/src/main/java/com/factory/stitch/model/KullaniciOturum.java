package com.factory.stitch.model;

import java.time.LocalDateTime;

/**
 * Giriş yapmış kullanıcının oturum verisini taşıyan değer nesnesi (Value
 * Object).
 *
 * GÜVENLİK:
 * - Ham şifre bu nesne içinde ASLA saklanmaz.
 * - Kimlik doğrulama tamamlandıktan sonra yalnızca kullanıcı ID'si,
 * adı ve rolü burada tutulur.
 * - Nesne immutable (değişmez) tasarlanmıştır; set metodu yoktur.
 */
public final class KullaniciOturum {

    private final int kullaniciId;
    private final String kullaniciAdi; // Tam ad (ad + soyad)
    private final String eposta;
    private final Rol rol;
    private final LocalDateTime girisZamani;

    /**
     * @param kullaniciId  DB'deki kullanici_id
     * @param kullaniciAdi Ad soyad (görüntüleme için)
     * @param eposta       E-posta adresi
     * @param rol          Kullanıcının rolü (Rol enum sabiti)
     */
    public KullaniciOturum(int kullaniciId,
            String kullaniciAdi,
            String eposta,
            Rol rol) {
        if (rol == null)
            throw new IllegalArgumentException("Rol boş olamaz.");
        this.kullaniciId = kullaniciId;
        this.kullaniciAdi = kullaniciAdi;
        this.eposta = eposta;
        this.rol = rol;
        this.girisZamani = LocalDateTime.now();
    }

    // ─── Erişimciler ─────────────────────────────────────────────────────────

    public int getKullaniciId() {
        return kullaniciId;
    }

    public String getKullaniciAdi() {
        return kullaniciAdi;
    }

    public String getEposta() {
        return eposta;
    }

    public Rol getRol() {
        return rol;
    }

    public LocalDateTime getGirisZamani() {
        return girisZamani;
    }

    // ─── Kısa Yardımcılar ────────────────────────────────────────────────────

    /** Kullanıcının bağlı olduğu departmanı döndürür. */
    public Rol.Departman getDepartman() {
        return rol.getDepartman();
    }

    /** Kullanıcının yetki seviyesini döndürür. */
    public Rol.Yetki getYetki() {
        return rol.getYetki();
    }

    /** Kullanıcı SUPER yetkisine sahip mi? (Genel Müdür veya Admin) */
    public boolean isSuperYetki() {
        return rol.getYetki() == Rol.Yetki.SUPER;
    }

    /** Kullanıcı en az YÖNETİCİ yetkisine sahip mi? */
    public boolean isYoneticiVeyaUstu() {
        return rol.getYetki() == Rol.Yetki.SUPER
                || rol.getYetki() == Rol.Yetki.YONETICI;
    }

    /** IT departmanına ait mi? (Audit log için kullanılır) */
    public boolean isBtPersonel() {
        return rol.getDepartman() == Rol.Departman.IT;
    }

    @Override
    public String toString() {
        return String.format("KullaniciOturum{id=%d, ad='%s', rol=%s, giris=%s}",
                kullaniciId, kullaniciAdi, rol.name(), girisZamani);
    }
}
