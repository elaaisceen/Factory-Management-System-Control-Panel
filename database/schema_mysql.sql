-- 6-7. Hafta: Veritabani Tasarimi ve SQL Scripti
-- Hedef DB: MySQL 8+

DROP DATABASE IF EXISTS fabrika_erp;
CREATE DATABASE fabrika_erp CHARACTER SET utf8mb4 COLLATE utf8mb4_turkish_ci;
USE fabrika_erp;

-- ─────────────────────────────────────────────────────────────────────────────
-- ROL Tablosu
-- Java Rol.java Enum sabitleri ile birebir eşleşir.
-- rol_id 1-14 aralığı yeni_kayit.html <option value> ile örtüşmelidir.
-- ─────────────────────────────────────────────────────────────────────────────
CREATE TABLE rol (
    rol_id          INT          AUTO_INCREMENT PRIMARY KEY,
    rol_adi         VARCHAR(80)  NOT NULL UNIQUE,
    gorunen_ad      VARCHAR(100) NOT NULL,
    yetki_seviyesi  ENUM('SUPER','YONETICI','PERSONEL') NOT NULL DEFAULT 'PERSONEL',
    departman_grup  VARCHAR(50)  NOT NULL
);


CREATE TABLE kullanici (
    kullanici_id     INT          AUTO_INCREMENT PRIMARY KEY,
    ad_soyad         VARCHAR(100) NOT NULL,
    kullanici_adi    VARCHAR(50)  NOT NULL UNIQUE,
    sifre_hash       VARCHAR(255) NOT NULL,
    rol_id           INT          NOT NULL,
    departman_id     INT          NULL,
    aktif            TINYINT(1)   NOT NULL DEFAULT 1,
    olusturma_tarihi DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_kullanici_rol       FOREIGN KEY (rol_id)       REFERENCES rol(rol_id),
    CONSTRAINT fk_kullanici_departman FOREIGN KEY (departman_id) REFERENCES departman(departman_id)
);


CREATE TABLE departman (
    departman_id INT AUTO_INCREMENT PRIMARY KEY,
    departman_adi VARCHAR(100) NOT NULL UNIQUE,
    aylik_butce DECIMAL(14,2) NOT NULL DEFAULT 0,
    sorumlu_kullanici_id INT NULL,
    CONSTRAINT fk_departman_kullanici FOREIGN KEY (sorumlu_kullanici_id) REFERENCES kullanici(kullanici_id)
);

CREATE TABLE personel (
    personel_id INT AUTO_INCREMENT PRIMARY KEY,
    departman_id INT NOT NULL,
    ad_soyad VARCHAR(100) NOT NULL,
    maas DECIMAL(12,2) NOT NULL DEFAULT 0,
    ise_giris_tarihi DATE NOT NULL,
    aktif TINYINT(1) NOT NULL DEFAULT 1,
    CONSTRAINT fk_personel_departman FOREIGN KEY (departman_id) REFERENCES departman(departman_id)
);

CREATE TABLE malzeme (
    malzeme_id INT AUTO_INCREMENT PRIMARY KEY,
    malzeme_adi VARCHAR(100) NOT NULL UNIQUE,
    stok_miktari INT NOT NULL DEFAULT 0,
    kritik_limit INT NOT NULL DEFAULT 0,
    birim VARCHAR(20) NOT NULL DEFAULT 'ADET',
    CHECK (stok_miktari >= 0),
    CHECK (kritik_limit >= 0)
);

CREATE TABLE stok_hareket (
    hareket_id INT AUTO_INCREMENT PRIMARY KEY,
    malzeme_id INT NOT NULL,
    kullanici_id INT NOT NULL,
    hareket_turu ENUM('GIRIS', 'CIKIS') NOT NULL,
    miktar INT NOT NULL,
    hareket_tarihi DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    aciklama VARCHAR(255) NULL,
    CONSTRAINT fk_stok_hareket_malzeme FOREIGN KEY (malzeme_id) REFERENCES malzeme(malzeme_id),
    CONSTRAINT fk_stok_hareket_kullanici FOREIGN KEY (kullanici_id) REFERENCES kullanici(kullanici_id),
    CHECK (miktar > 0)
);

CREATE TABLE uretim_plan (
    plan_id INT AUTO_INCREMENT PRIMARY KEY,
    departman_id INT NOT NULL,
    proje_kodu VARCHAR(50) NOT NULL,
    plan_tarihi DATE NOT NULL,
    durum ENUM('PLANLANDI', 'DEVAM', 'TAMAMLANDI', 'IPTAL') NOT NULL DEFAULT 'PLANLANDI',
    CONSTRAINT fk_uretim_plan_departman FOREIGN KEY (departman_id) REFERENCES departman(departman_id)
);

CREATE TABLE tedarikci (
    tedarikci_id INT AUTO_INCREMENT PRIMARY KEY,
    tedarikci_adi VARCHAR(150) NOT NULL UNIQUE,
    yetkili_kisi VARCHAR(100) NULL,
    telefon VARCHAR(20) NULL,
    email VARCHAR(150) NULL,
    ulke VARCHAR(80) NOT NULL DEFAULT 'Türkiye',
    aktif TINYINT(1) NOT NULL DEFAULT 1,
    kayit_tarihi DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE satin_alma_siparis (
    siparis_id INT AUTO_INCREMENT PRIMARY KEY,
    tedarikci_id INT NOT NULL,
    malzeme_id INT NOT NULL,
    miktar INT NOT NULL,
    birim_fiyat DECIMAL(12,2) NOT NULL DEFAULT 0,
    toplam_tutar DECIMAL(14,2) GENERATED ALWAYS AS (miktar * birim_fiyat) STORED,
    durum ENUM('BEKLIYOR','ONAYLANDI','YOLDA','TESLIM_EDILDI','IPTAL') NOT NULL DEFAULT 'BEKLIYOR',
    siparis_tarihi DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    tahmini_teslim DATE NULL,
    notlar VARCHAR(500) NULL,
    CONSTRAINT fk_siparis_tedarikci FOREIGN KEY (tedarikci_id) REFERENCES tedarikci(tedarikci_id),
    CONSTRAINT fk_siparis_malzeme FOREIGN KEY (malzeme_id) REFERENCES malzeme(malzeme_id),
    CHECK (miktar > 0),
    CHECK (birim_fiyat >= 0)
);

CREATE TABLE satinalma_talep (
    talep_id INT AUTO_INCREMENT PRIMARY KEY,
    departman_id INT NOT NULL,
    malzeme_id INT NOT NULL,
    talep_miktari INT NOT NULL,
    durum ENUM('BEKLIYOR', 'ONAYLANDI', 'REDDEDILDI', 'TEDARIK_EDILDI') NOT NULL DEFAULT 'BEKLIYOR',
    talep_tarihi DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_satinalma_departman FOREIGN KEY (departman_id) REFERENCES departman(departman_id),
    CONSTRAINT fk_satinalma_malzeme FOREIGN KEY (malzeme_id) REFERENCES malzeme(malzeme_id),
    CHECK (talep_miktari > 0)
);

CREATE TABLE muhasebe_kayit (
    kayit_id INT AUTO_INCREMENT PRIMARY KEY,
    departman_id INT NOT NULL,
    tutar DECIMAL(14,2) NOT NULL,
    kayit_turu ENUM('GELIR', 'GIDER') NOT NULL,
    kayit_tarihi DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    aciklama VARCHAR(255) NULL,
    CONSTRAINT fk_muhasebe_departman FOREIGN KEY (departman_id) REFERENCES departman(departman_id),
    CHECK (tutar >= 0)
);

CREATE INDEX idx_kullanici_rol ON kullanici(rol_id);
CREATE INDEX idx_personel_departman ON personel(departman_id);
CREATE INDEX idx_stok_hareket_malzeme ON stok_hareket(malzeme_id);
CREATE INDEX idx_stok_hareket_tarih ON stok_hareket(hareket_tarihi);
CREATE INDEX idx_uretim_plan_departman ON uretim_plan(departman_id);
CREATE INDEX idx_talep_departman ON satinalma_talep(departman_id);
CREATE INDEX idx_muhasebe_departman ON muhasebe_kayit(departman_id);
CREATE INDEX idx_siparis_tedarikci ON satin_alma_siparis(tedarikci_id);
CREATE INDEX idx_siparis_malzeme ON satin_alma_siparis(malzeme_id);
CREATE INDEX idx_siparis_durum ON satin_alma_siparis(durum);
CREATE INDEX idx_tedarikci_aktif ON tedarikci(aktif);


-- 14 Rol — Java Rol.java Enum ve yeni_kayit.html <option value> ile birebir eşleşir
INSERT INTO rol (rol_id, rol_adi, gorunen_ad, yetki_seviyesi, departman_grup) VALUES
(1,  'GENEL_MUDUR',        'Genel Müdr',                  'SUPER',    'YONETIM'),
(2,  'GELISTIRICI_ADMIN',  'Geliştirici / Admin',          'SUPER',    'IT'),
(3,  'BT_PERSONEL',        'BT Personeli',                'PERSONEL', 'IT'),
(4,  'BT_YONETICI',        'BT Yöneticisi',               'YONETICI', 'IT'),
(5,  'IK_PERSONEL',        'İnsan Kaynakları Personeli',   'PERSONEL', 'IK'),
(6,  'IK_YONETICI',        'İnsan Kaynakları Yöneticisi',  'YONETICI', 'IK'),
(7,  'DEPO_PERSONEL',      'Depo Personeli',              'PERSONEL', 'DEPO'),
(8,  'DEPO_YONETICI',      'Depo Yöneticisi',             'YONETICI', 'DEPO'),
(9,  'URETIM_PERSONEL',    'Üretim Personeli',             'PERSONEL', 'URETIM'),
(10, 'URETIM_YONETICI',    'Üretim Yöneticisi',            'YONETICI', 'URETIM'),
(11, 'FINANS_PERSONEL',    'Finans Personeli',            'PERSONEL', 'FINANS'),
(12, 'FINANS_YONETICI',    'Finans Yöneticisi',           'YONETICI', 'FINANS'),
(13, 'SATINALMA_PERSONEL', 'Satın Alma Personeli',        'PERSONEL', 'SATINALMA'),
(14, 'SATINALMA_YONETICI', 'Satın Alma Yöneticisi',       'YONETICI', 'SATINALMA');


-- Yeni kullanici tablosu departman_id sütununu da içeriyor
INSERT INTO kullanici (ad_soyad, kullanici_adi, sifre_hash, rol_id, departman_id) VALUES
('Ahmet Yilmaz',  'ahmet.ik',         'ornek_hash_ik',         5,  1),   -- IK_PERSONEL, IK dept
('Mehmet Kaya',   'mehmet.uretim',    'ornek_hash_uretim',     9,  2),   -- URETIM_PERSONEL
('Ali Riza',      'ali.depo',         'ornek_hash_depo',       7,  3),   -- DEPO_PERSONEL
('Ela Eda',       'ela.bt',           'ornek_hash_bt',         3,  4),   -- BT_PERSONEL
('Gizem Sen',     'gizem.satinalma',  'ornek_hash_satinalma',  13, 5),   -- SATINALMA_PERSONEL
('Aylin Tuna',    'aylin.finans',     'ornek_hash_finans',     11, 6),   -- FINANS_PERSONEL
('Genel Mudur',   'mudur',            'ornek_hash_mudur',      1,  NULL); -- GENEL_MUDUR


INSERT INTO departman (departman_adi, aylik_butce, sorumlu_kullanici_id) VALUES
('Insan Kaynaklari', 250000.00, 1),
('Uretim', 950000.00, 2),
('Depo ve Stok', 300000.00, 3),
('IT', 220000.00, 4),
('Satin Alma', 450000.00, 5),
('Finans', 350000.00, 6);

INSERT INTO malzeme (malzeme_adi, stok_miktari, kritik_limit, birim) VALUES
('Demir Profil', 1200, 300, 'ADET'),
('Celik Vida', 5000, 1000, 'ADET'),
('Bakir Kablo', 800, 250, 'METRE'),
('Aluminyum Alaşım A-202', 12, 50, 'KG'),
('Hassas Valf X-1', 8, 20, 'ADET'),
('Karbon Celik Levha', 82, 120, 'ADET'),
('Hidrolik Conta C-9', 230, 200, 'ADET');

INSERT INTO tedarikci (tedarikci_adi, yetkili_kisi, telefon, email, ulke) VALUES
('Global Metallics Inc.', 'John Smith', '+1-555-0100', 'john@globalmetallics.com', 'ABD'),
('Precision Parts EU', 'Hans Müller', '+49-30-0200', 'hans@precisioneu.de', 'Almanya'),
('Stark Endüstri A.Ş.', 'Ahmet Yıldız', '+90-212-3300', 'ahmet@stark.com.tr', 'Türkiye'),
('SteelCo Ltd.', 'Emma Wilson', '+44-20-0400', 'emma@steelco.co.uk', 'Birleşik Krallık'),
('MechX Tedarik', 'Marco Rossi', '+39-06-0500', 'marco@mechx.it', 'İtalya');

INSERT INTO satin_alma_siparis (tedarikci_id, malzeme_id, miktar, birim_fiyat, durum, tahmini_teslim, notlar) VALUES
(1, 1, 500, 45.00, 'YOLDA', '2026-10-24', 'PO-9821: Demir Profil siparişi'),
(2, 5, 20, 1250.00, 'YOLDA', '2026-10-15', 'PO-88304: Hassas Valf yenileme'),
(3, 4, 200, 85.50, 'TESLIM_EDILDI', '2026-04-20', 'Teslim edildi, kalite kontrolden geçti'),
(4, 6, 50, 320.00, 'BEKLIYOR', '2026-11-05', 'Onay bekliyor');

INSERT INTO personel (departman_id, ad_soyad, maas, ise_giris_tarihi, aktif) VALUES
(1, 'Veli Demir', 32000.00, '2025-01-10', 1),
(2, 'Burak Usta', 38000.00, '2024-11-03', 1),
(3, 'Deniz Yildiz', 30000.00, '2025-02-20', 1);

-- Örnek sorgular
-- Departman bazlı personel sayısı:
-- SELECT d.departman_adi, COUNT(p.personel_id) personel_sayisi
-- FROM departman d LEFT JOIN personel p ON p.departman_id = d.departman_id
-- GROUP BY d.departman_id;

-- Kritik stok altındaki malzemeler:
-- SELECT m.malzeme_adi, m.stok_miktari, m.kritik_limit
-- FROM malzeme m WHERE m.stok_miktari <= m.kritik_limit;

-- Tedarikçi bazlı sipariş özeti:
-- SELECT t.tedarikci_adi, COUNT(s.siparis_id) siparis_sayisi,
--        SUM(s.toplam_tutar) toplam_tutar, s.durum
-- FROM tedarikci t
-- LEFT JOIN satin_alma_siparis s ON s.tedarikci_id = t.tedarikci_id
-- GROUP BY t.tedarikci_id, s.durum;

-- Bekleyen satın alma siparişleri:
-- SELECT s.siparis_id, t.tedarikci_adi, m.malzeme_adi,
--        s.miktar, s.birim_fiyat, s.toplam_tutar, s.tahmini_teslim
-- FROM satin_alma_siparis s
-- JOIN tedarikci t ON t.tedarikci_id = s.tedarikci_id
-- JOIN malzeme m ON m.malzeme_id = s.malzeme_id
-- WHERE s.durum IN ('BEKLIYOR', 'ONAYLANDI', 'YOLDA')
-- ORDER BY s.tahmini_teslim ASC;

-- ─────────────────────────────────────────────────────────────────────────────
-- VIEW: v_satin_alma_ozet
-- Satın alma siparişlerini tedarikçi ve malzeme bilgileriyle birleştirerek
-- raporlamayı kolaylaştıran özet görünüm.
-- "satin_alma_id" referansı bu VIEW üzerinden yapılır; ham tablo adı
-- "siparis_id" olduğundan bu VIEW her ikisini de sunar.
-- ─────────────────────────────────────────────────────────────────────────────
CREATE OR REPLACE VIEW v_satin_alma_ozet AS
SELECT
    s.siparis_id                        AS satin_alma_id,   -- Raporlarda kullanılan takma ad
    s.siparis_id,
    t.tedarikci_adi,
    t.email                             AS tedarikci_email,
    m.malzeme_adi,
    m.birim,
    s.miktar,
    s.birim_fiyat,
    s.toplam_tutar,
    s.durum,
    s.siparis_tarihi,
    s.tahmini_teslim,
    s.notlar
FROM satin_alma_siparis s
JOIN tedarikci t ON t.tedarikci_id = s.tedarikci_id
JOIN malzeme   m ON m.malzeme_id   = s.malzeme_id;

-- ─────────────────────────────────────────────────────────────────────────────
-- VIEW: v_kritik_stok
-- Stok miktarı kritik limitin altındaki malzemeleri gösterir.
-- ─────────────────────────────────────────────────────────────────────────────
CREATE OR REPLACE VIEW v_kritik_stok AS
SELECT
    malzeme_id,
    malzeme_adi,
    stok_miktari,
    kritik_limit,
    birim,
    (kritik_limit - stok_miktari) AS eksik_miktar
FROM malzeme
WHERE stok_miktari <= kritik_limit
ORDER BY eksik_miktar DESC;

-- NOT: DatabaseConnection.java artık DB adı olarak "fabrika_erp" kullanmaktadır.
-- Aşağıdaki şema bu veritabanı üzerinde çalışacak şekilde tasarlanmıştır:
--   USE fabrika_erp;
-- Bağlantı URL'si:
--   jdbc:mysql://localhost:3306/fabrika_erp?...
