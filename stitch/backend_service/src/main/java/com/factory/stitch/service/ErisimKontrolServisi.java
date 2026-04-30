package com.factory.stitch.service;

import com.factory.stitch.model.KullaniciOturum;
import com.factory.stitch.model.Rol;
import com.factory.stitch.model.Rol.Departman;
import com.factory.stitch.model.Rol.Yetki;
import com.factory.stitch.util.DosyaKayitUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Merkezi Erişim Kontrol Servisi — "Bu kullanıcı bu işlemi yapabilir mi?"
 *
 * <h3>Yetkilendirme Mantığı</h3>
 * <pre>
 *  Kural 1 – SUPER yetkisi:
 *    GENEL_MUDUR ve GELISTIRICI_ADMIN her şeyi görebilir ve yapabilir.
 *
 *  Kural 2 – YONETICI yetkisi:
 *    Kendi departmanını yönetir. Diğer departmanların özetini okuyabilir
 *    ama yazma işlemi yapamaz.
 *
 *  Kural 3 – PERSONEL yetkisi:
 *    Yalnızca kendi departmanındaki kendi kayıtlarını görebilir.
 *
 *  Kural 4 – IT İstisnası (Audit Zorunluluğu):
 *    IT departmanı her yere erişebilir (YONETICI gibi) ama yaptığı
 *    her işlem otomatik olarak audit log'a yazılır.
 * </pre>
 *
 * <h3>Spring Security Entegrasyonu</h3>
 * Spring Security aktifse Controller metodlarına şu notları ekleyin:
 * <pre>
 *  // Genel Müdür + Admin + IK Yöneticisi
 *  {@literal @}PreAuthorize("hasAnyRole('GENEL_MUDUR','GELISTIRICI_ADMIN','IK_YONETICI')")
 *
 *  // Herhangi bir yönetici
 *  {@literal @}PreAuthorize("hasAnyRole('GENEL_MUDUR','GELISTIRICI_ADMIN',
 *                    'IK_YONETICI','DEPO_YONETICI','URETIM_YONETICI',
 *                    'FINANS_YONETICI','SATINALMA_YONETICI','BT_YONETICI')")
 * </pre>
 * Spring Security yoksa bu servisi doğrudan kullanın (B. Manuel Kontrol).
 */
public class ErisimKontrolServisi {

    private static final DateTimeFormatter ZAMAN = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    // ─── Temel Erişim Soruları (true/false) ──────────────────────────────────

    /**
     * Kullanıcı bu departmanın verisine erişebilir mi?
     *
     * @param kullanici Oturum açmış kullanıcı
     * @param hedefDep  Erişilmek istenen departman
     * @return true → erişime izin ver
     */
    public boolean erisebilirMi(KullaniciOturum kullanici, Departman hedefDep) {
        // Kural 1: SUPER her yere girer
        if (kullanici.isSuperYetki()) {
            return true;
        }

        // Kural 4: IT her yere girer ama loglanır
        if (kullanici.isBtPersonel()) {
            itAuditLogYaz(kullanici, hedefDep.name(), "OKUMA");
            return true;
        }

        // Kural 2 & 3: Kendi departmanı
        boolean kendisiDep = kullanici.getDepartman() == hedefDep;

        // PERSONEL: yalnızca kendi departmanı
        if (kullanici.getYetki() == Yetki.PERSONEL) {
            return kendisiDep;
        }

        // YONETICI: kendi departmanı + diğerlerini okuyabilir (sadece okuma)
        return true; // Yazma kısıtlaması yazabilirMi() metodunda
    }

    /**
     * Kullanıcı bu departman üzerinde yazma işlemi yapabilir mi?
     * (Kayıt ekleme, güncelleme, silme)
     *
     * @param kullanici Oturum açmış kullanıcı
     * @param hedefDep  Değiştirilmek istenen departman
     * @return true → yazma izni var
     */
    public boolean yazabilirMi(KullaniciOturum kullanici, Departman hedefDep) {
        // Kural 1: SUPER her şeyi yapabilir
        if (kullanici.isSuperYetki()) {
            return true;
        }

        // Kural 4: IT yazarsa MUTLAKA loglanır
        if (kullanici.isBtPersonel()) {
            itAuditLogYaz(kullanici, hedefDep.name(), "YAZMA");
            // BT kendi departmanını yönetebilir, diğerlerine yalnızca okuma
            return kullanici.getDepartman() == hedefDep
                || kullanici.getYetki() == Yetki.YONETICI;
        }

        // Kural 2: YONETICI kendi departmanına yazar
        if (kullanici.getYetki() == Yetki.YONETICI) {
            return kullanici.getDepartman() == hedefDep;
        }

        // Kural 3: PERSONEL yalnızca kendi kayıtlarını günceller
        // (bu kontrolü çağıran servis kendi ID filtresiyle yapmalı)
        return kullanici.getDepartman() == hedefDep;
    }

    /**
     * Kullanıcı rapor modülüne erişebilir mi?
     * PERSONEL kendi departman raporunu görebilir.
     * YONETICI/SUPER tüm raporları görebilir.
     */
    public boolean raporGorebilirMi(KullaniciOturum kullanici, Departman raporDep) {
        if (kullanici.isSuperYetki()) return true;
        if (kullanici.isBtPersonel()) {
            itAuditLogYaz(kullanici, raporDep.name(), "RAPOR_OKUMA");
            return true;
        }
        return kullanici.getDepartman() == raporDep;
    }

    // ─── Kontrol + Fırlat ────────────────────────────────────────────────────

    /**
     * Erişim yoksa {@link ErisimReddedildiException} fırlatır.
     * Controller'larda şöyle kullanılır:
     * <pre>
     *   erisimKontrol.dogrulaErisim(oturum, Departman.IK);
     * </pre>
     */
    public void dogrulaErisim(KullaniciOturum kullanici, Departman hedefDep) {
        if (!erisebilirMi(kullanici, hedefDep)) {
            throw new ErisimReddedildiException(kullanici, hedefDep, "OKUMA");
        }
    }

    /** Yazma izni yoksa fırlatır. */
    public void dogrulaYazma(KullaniciOturum kullanici, Departman hedefDep) {
        if (!yazabilirMi(kullanici, hedefDep)) {
            throw new ErisimReddedildiException(kullanici, hedefDep, "YAZMA");
        }
    }

    // ─── Veri Filtreleme Yardımcısı ──────────────────────────────────────────

    /**
     * SQL sorgusuna eklenmesi gereken departman ID filtresini döndürür.
     *
     * <pre>
     * Kullanım (servis katmanında):
     *   String filtre = erisimKontrol.departmanFiltresi(oturum);
     *   // filtre = "" (SUPER) veya "AND departman_id = 5" (PERSONEL)
     *   String sql = "SELECT * FROM uretim_kayitlari WHERE aktif = 1 " + filtre;
     * </pre>
     *
     * @param kullanici Oturum açmış kullanıcı
     * @return SQL WHERE eki (boş string = filtre yok)
     */
    public String departmanFiltresi(KullaniciOturum kullanici) {
        if (kullanici.isSuperYetki() || kullanici.isBtPersonel()) {
            return ""; // Kısıtsız erişim
        }
        // PERSONEL veya YONETICI: kendi departmanıyla sınırlı
        // Gerçek uygulamada departman_id DB'den alınmalı; bu örnek Enum adını kullanır.
        return " AND departman_adi = '" + kullanici.getDepartman().getSqlAdi() + "'";
    }

    // ─── IT Audit Log ────────────────────────────────────────────────────────

    /**
     * IT personelinin her erişimini hem konsola hem log dosyasına yazar.
     *
     * Kural: IT departmanı her şeyi görebilir ama yaptığı her işlem
     * "IT Personeli X, Y verisine Z işlemi yaptı" biçiminde kaydedilir.
     */
    private void itAuditLogYaz(KullaniciOturum kullanici, String hedef, String islem) {
        String satir = String.format("[IT-AUDIT][%s] Kullanıcı: %s (ID:%d, Rol:%s) → Hedef: %s → İşlem: %s",
                LocalDateTime.now().format(ZAMAN),
                kullanici.getKullaniciAdi(),
                kullanici.getKullaniciId(),
                kullanici.getRol().name(),
                hedef,
                islem);
        System.out.println(satir);
        DosyaKayitUtil.logSatiriEkle(satir);
    }

    // ─── Erişim Reddedildi İstisnası ─────────────────────────────────────────

    /**
     * Yetki hatası için özelleştirilmiş istisna.
     * Controller'da HTTP 403 yanıtına dönüştürülmelidir.
     */
    public static class ErisimReddedildiException extends RuntimeException {
        private final KullaniciOturum kullanici;
        private final Departman       hedefDep;
        private final String          islemTuru;

        public ErisimReddedildiException(KullaniciOturum k, Departman dep, String islem) {
            super(String.format(
                    "ERİŞİM REDDEDİLDİ: %s (Rol: %s) → %s departmanına %s yapamaz.",
                    k.getKullaniciAdi(), k.getRol().name(), dep.name(), islem));
            this.kullanici = k;
            this.hedefDep  = dep;
            this.islemTuru = islem;
        }

        public KullaniciOturum getKullanici() { return kullanici; }
        public Departman       getHedefDep()  { return hedefDep; }
        public String          getIslemTuru() { return islemTuru; }
    }
}
