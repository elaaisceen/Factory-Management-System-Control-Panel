package com.factory.stitch.model;

/**
 * Sistemdeki tüm kullanıcı rollerini tanımlayan Enum.
 *
 * Değerler, frontend'deki {@code <option value="N">} sayıları ile
 * birebir eşleşir (yeni_kayit.html).
 *
 * <pre>
 *  ID │ Enum Sabiti             │ Görünen Ad                    │ Departman
 *  ───┼─────────────────────────┼───────────────────────────────┼──────────────────
 *   1 │ GENEL_MUDUR             │ Genel Müdür                   │ Yönetim
 *   2 │ GELISTIRICI_ADMIN       │ Geliştirici / Admin           │ IT
 *   3 │ BT_PERSONEL             │ BT Personeli                  │ IT
 *   4 │ BT_YONETICI             │ BT Yöneticisi                 │ IT
 *   5 │ IK_PERSONEL             │ İnsan Kaynakları Personeli    │ İK
 *   6 │ IK_YONETICI             │ İnsan Kaynakları Yöneticisi   │ İK
 *   7 │ DEPO_PERSONEL           │ Depo Personeli                │ Depo
 *   8 │ DEPO_YONETICI           │ Depo Yöneticisi               │ Depo
 *   9 │ URETIM_PERSONEL         │ Üretim Personeli              │ Üretim
 *  10 │ URETIM_YONETICI         │ Üretim Yöneticisi             │ Üretim
 *  11 │ FINANS_PERSONEL         │ Finans Personeli              │ Finans
 *  12 │ FINANS_YONETICI         │ Finans Yöneticisi             │ Finans
 *  13 │ SATINALMA_PERSONEL      │ Satın Alma Personeli          │ Satın Alma
 *  14 │ SATINALMA_YONETICI      │ Satın Alma Yöneticisi         │ Satın Alma
 * </pre>
 */
public enum Rol {

    // ─── Üst Yönetim ────────────────────────────────────────────────────────
    GENEL_MUDUR(1,        "Genel Müdür",                  Departman.YONETIM,    Yetki.SUPER),
    GELISTIRICI_ADMIN(2,  "Geliştirici / Admin",          Departman.IT,         Yetki.SUPER),

    // ─── Bilgi Teknolojileri ─────────────────────────────────────────────────
    BT_PERSONEL(3,        "BT Personeli",                 Departman.IT,         Yetki.PERSONEL),
    BT_YONETICI(4,        "BT Yöneticisi",                Departman.IT,         Yetki.YONETICI),

    // ─── İnsan Kaynakları ────────────────────────────────────────────────────
    IK_PERSONEL(5,        "İnsan Kaynakları Personeli",   Departman.IK,         Yetki.PERSONEL),
    IK_YONETICI(6,        "İnsan Kaynakları Yöneticisi",  Departman.IK,         Yetki.YONETICI),

    // ─── Depo & Stok ─────────────────────────────────────────────────────────
    DEPO_PERSONEL(7,      "Depo Personeli",               Departman.DEPO,       Yetki.PERSONEL),
    DEPO_YONETICI(8,      "Depo Yöneticisi",              Departman.DEPO,       Yetki.YONETICI),

    // ─── Üretim ──────────────────────────────────────────────────────────────
    URETIM_PERSONEL(9,    "Üretim Personeli",             Departman.URETIM,     Yetki.PERSONEL),
    URETIM_YONETICI(10,   "Üretim Yöneticisi",            Departman.URETIM,     Yetki.YONETICI),

    // ─── Finans ──────────────────────────────────────────────────────────────
    FINANS_PERSONEL(11,   "Finans Personeli",             Departman.FINANS,     Yetki.PERSONEL),
    FINANS_YONETICI(12,   "Finans Yöneticisi",            Departman.FINANS,     Yetki.YONETICI),

    // ─── Satın Alma ──────────────────────────────────────────────────────────
    SATINALMA_PERSONEL(13,"Satın Alma Personeli",         Departman.SATINALMA,  Yetki.PERSONEL),
    SATINALMA_YONETICI(14,"Satın Alma Yöneticisi",        Departman.SATINALMA,  Yetki.YONETICI);

    // ─── Alanlar ─────────────────────────────────────────────────────────────

    /** Frontend'deki <option value="N"> ile eşleşen sayısal kimlik. */
    private final int     id;
    /** Kullanıcıya gösterilen Türkçe etiket. */
    private final String  gorünenAd;
    /** Bu rolün bağlı olduğu departman. */
    private final Departman departman;
    /** Rolün genel yetki seviyesi. */
    private final Yetki   yetki;

    Rol(int id, String gorünenAd, Departman departman, Yetki yetki) {
        this.id        = id;
        this.gorünenAd = gorünenAd;
        this.departman = departman;
        this.yetki     = yetki;
    }

    // ─── Erişimciler ─────────────────────────────────────────────────────────

    public int        getId()        { return id; }
    public String     getGorünenAd() { return gorünenAd; }
    public Departman  getDepartman() { return departman; }
    public Yetki      getYetki()     { return yetki; }

    // ─── Yardımcı Metodlar ───────────────────────────────────────────────────

    /**
     * Frontend'den gelen sayısal ID'yi Enum sabitine çevirir.
     *
     * @param id 1–14 arasında rol ID'si
     * @return Karşılık gelen Rol sabiti
     * @throws IllegalArgumentException Geçersiz ID girilirse
     */
    public static Rol fromId(int id) {
        for (Rol r : values()) {
            if (r.id == id) return r;
        }
        throw new IllegalArgumentException("Geçersiz rol ID'si: " + id);
    }

    /** Spring Security'de kullanılan "ROLE_XXX" biçimini döndürür. */
    public String toSpringRole() {
        return "ROLE_" + name();
    }

    // ─── Yetki Seviyesi (iç enum) ────────────────────────────────────────────

    /**
     * Rolün genel erişim kademesi.
     * SUPER > YONETICI > PERSONEL
     */
    public enum Yetki {
        /** Sistemin tamamına erişir, tüm departmanları görür. */
        SUPER,
        /** Kendi departmanını yönetir; diğer departmanların özetini görebilir. */
        YONETICI,
        /** Yalnızca kendi departmanı ve kendi kayıtları. */
        PERSONEL
    }

    // ─── Departman (iç enum) ─────────────────────────────────────────────────

    /**
     * Sistemdeki departmanlar.
     * Değerler schema_mysql.sql'deki departman tablosu ile uyumludur.
     */
    public enum Departman {
        YONETIM,
        IT,
        IK,
        DEPO,
        URETIM,
        FINANS,
        SATINALMA;

        /**
         * Departmanın schema_mysql.sql'deki Türkçe adını döndürür.
         * (departman tablosundaki departman_adi sütunu ile eşleşir)
         */
        public String getSqlAdi() {
              return "sql_tablo_adi";
        } 
         
        public String getDepartmanAdi() {
            switch (this) {
                case YONETIM:
                    return "Yönetim";
                case IT:
                    return "IT";
                case IK:
                    return "Insan Kaynaklari";
                case DEPO:
                    return "Depo ve Stok";
                case URETIM:
                    return "Uretim";
                case FINANS:
                    return "Finans";
                case SATINALMA:
                    return "Satin Alma";
                default:
                    return "Bilinmeyen Departman";
            }
        }
    }
}
