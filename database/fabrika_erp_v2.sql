-- ============================================================
--  FABRIKA ERP - Modernize Edilmiş Veritabanı Scripti
--  Versiyon : 2.0
--  Karakter  : utf8mb4 / utf8mb4_turkish_ci
--  Standart  : 3NF | RBAC (14 Rol) | IT Audit | DECIMAL(19,4)
--  Oluşturma : 2026-04-29
-- ============================================================

SET FOREIGN_KEY_CHECKS = 0;
DROP DATABASE IF EXISTS `fabrika_erp`;
CREATE DATABASE `fabrika_erp` CHARACTER SET utf8mb4 COLLATE utf8mb4_turkish_ci;
USE `fabrika_erp`;

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET FOREIGN_KEY_CHECKS = 0;
START TRANSACTION;
SET time_zone = "+03:00";
SET NAMES utf8mb4 COLLATE utf8mb4_turkish_ci;

-- VERİTABANI OLUŞTURMA
DROP DATABASE IF EXISTS `fabrika_erp`;
CREATE DATABASE `fabrika_erp`
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_turkish_ci;
USE `fabrika_erp`;

-- ============================================================
-- 1. ROL TABLOSU (Java Enum ve HTML Seçenekleri ile Uyumlu)
-- ============================================================
CREATE TABLE `rol` (
  `rol_id`           INT(11)      NOT NULL AUTO_INCREMENT,
  `rol_adi`          VARCHAR(80)  NOT NULL,
  `yetki_seviyesi`   TINYINT(2)   NOT NULL,
  `aciklama`         VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`rol_id`),
  UNIQUE KEY `uq_rol_adi` (`rol_adi`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_turkish_ci;

INSERT INTO `rol` (`rol_id`, `rol_adi`, `yetki_seviyesi`, `aciklama`) VALUES
(1,  'GENEL_MUDUR',        10, 'Genel Müdür - Tüm modüllere tam erişim'),
(2,  'GELISTIRICI_ADMIN',  9,  'Geliştirici / Admin - Sistem yönetimi ve BT'),
(3,  'BT_PERSONEL',        4,  'BT Personeli - Teknik destek ve bakım'),
(4,  'BT_YONETICI',        9,  'BT Yöneticisi - Sistem yönetimi ve log erişimi'),
(5,  'IK_PERSONEL',        4,  'İnsan Kaynakları Personeli - Personel bilgileri'),
(6,  'IK_YONETICI',        8,  'İnsan Kaynakları Yöneticisi - Bordro ve İK yönetimi'),
(7,  'DEPO_PERSONEL',      4,  'Depo Personeli - Stok giriş/çıkış'),
(8,  'DEPO_YONETICI',      6,  'Depo Yöneticisi - Stok hareketleri takibi'),
(9,  'URETIM_PERSONEL',    4,  'Üretim Personeli - Plan görüntüleme'),
(10, 'URETIM_YONETICI',    7,  'Üretim Yöneticisi - Üretim planları ve malzeme'),
(11, 'FINANS_PERSONEL',    5,  'Finans Personeli - Muhasebe kayıt girişi'),
(12, 'FINANS_YONETICI',    8,  'Finans Yöneticisi - Finansal raporlar ve onaylar'),
(13, 'SATINALMA_PERSONEL', 5,  'Satın Alma Personeli - Talep oluşturma'),
(14, 'SATINALMA_YONETICI', 7,  'Satın Alma Yöneticisi - Satın alma onayları');

-- ============================================================
-- 2. KULLANICI TABLOSU
-- ============================================================
CREATE TABLE `kullanici` (
  `kullanici_id`    INT(11)      NOT NULL AUTO_INCREMENT,
  `adi`             VARCHAR(100) NOT NULL,
  `soyadi`          VARCHAR(100) NOT NULL,
  `kullanici_adi`   VARCHAR(100) NOT NULL COMMENT 'Login username',
  `sifre_hash`      VARCHAR(255) NOT NULL COMMENT 'BCrypt hash - min 255 karakter',
  `rol_id`          INT(11)      NOT NULL,
  `aktif`           TINYINT(1)   NOT NULL DEFAULT 1,
  `eposta`          VARCHAR(150) DEFAULT NULL,
  `son_giris`       DATETIME     DEFAULT NULL,
  `basarisiz_giris` TINYINT(3)   NOT NULL DEFAULT 0 COMMENT 'Başarısız giriş sayacı',
  `kayit_tarihi`    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP(),
  PRIMARY KEY (`kullanici_id`),
  UNIQUE KEY `uq_kullanici_adi` (`kullanici_adi`),
  KEY `fk_kullanici_rol` (`rol_id`),
  CONSTRAINT `fk_kullanici_rol`
    FOREIGN KEY (`rol_id`) REFERENCES `rol` (`rol_id`)
    ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_turkish_ci
  COMMENT='Sistem kullanıcıları ve kimlik bilgileri';

-- ============================================================
-- 3. DEPARTMAN TABLOSU
-- ============================================================
CREATE TABLE `departman` (
  `departman_id`          INT(11)        NOT NULL AUTO_INCREMENT,
  `departman_adi`         VARCHAR(100)   NOT NULL,
  `aylik_butce`           DECIMAL(19,4)  NOT NULL DEFAULT 0.0000,
  `harcanan_butce`        DECIMAL(19,4)  NOT NULL DEFAULT 0.0000,
  `sorumlu_kullanici_id`  INT(11)        NOT NULL,
  `maliyet_merkezi_kodu`  VARCHAR(20)    DEFAULT NULL,
  `aktif`                 TINYINT(1)     NOT NULL DEFAULT 1,
  PRIMARY KEY (`departman_id`),
  UNIQUE KEY `uq_departman_adi` (`departman_adi`),
  KEY `fk_departman_kullanici` (`sorumlu_kullanici_id`),
  CONSTRAINT `fk_departman_kullanici`
    FOREIGN KEY (`sorumlu_kullanici_id`) REFERENCES `kullanici` (`kullanici_id`)
    ON UPDATE CASCADE,
  CONSTRAINT `chk_departman_butce`
    CHECK (`aylik_butce` >= 0 AND `harcanan_butce` >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_turkish_ci;

-- ============================================================
-- 4. PERSONEL TABLOSU
-- ============================================================
CREATE TABLE `personel` (
  `personel_id`       INT(11)       NOT NULL AUTO_INCREMENT,
  `departman_id`      INT(11)       NOT NULL,
  `kullanici_id`      INT(11)       DEFAULT NULL,
  `adi`               VARCHAR(100)  NOT NULL,
  `soyadi`            VARCHAR(100)  NOT NULL,
  `tc_kimlik_no`      CHAR(11)      DEFAULT NULL,
  `maas`              DECIMAL(19,4) NOT NULL DEFAULT 0.0000,
  `sgk_prim_orani`    DECIMAL(5,4)  NOT NULL DEFAULT 0.1500,
  `vergi_orani`       DECIMAL(5,4)  NOT NULL DEFAULT 0.2000,
  `ise_giris_tarihi`  DATE          NOT NULL,
  `calisma_baslangic` TIME          NOT NULL DEFAULT '08:00:00',
  `calisma_bitis`     TIME          NOT NULL DEFAULT '17:00:00',
  `aktif`             TINYINT(1)    NOT NULL DEFAULT 1,
  PRIMARY KEY (`personel_id`),
  UNIQUE KEY `uq_tc_kimlik` (`tc_kimlik_no`),
  KEY `fk_personel_departman` (`departman_id`),
  KEY `fk_personel_kullanici` (`kullanici_id`),
  CONSTRAINT `fk_personel_departman`
    FOREIGN KEY (`departman_id`) REFERENCES `departman` (`departman_id`) ON UPDATE CASCADE,
  CONSTRAINT `fk_personel_kullanici`
    FOREIGN KEY (`kullanici_id`) REFERENCES `kullanici` (`kullanici_id`) ON UPDATE CASCADE ON DELETE SET NULL,
  CONSTRAINT `chk_personel_maas` CHECK (`maas` >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_turkish_ci;

-- ============================================================
-- 5. TEDARİKÇİ TABLOSU (YENİ)
-- ============================================================
CREATE TABLE `tedarikci` (
  `tedarikci_id`   INT(11)       NOT NULL AUTO_INCREMENT,
  `firma_adi`      VARCHAR(150)  NOT NULL,
  `vergi_no`       VARCHAR(20)   DEFAULT NULL,
  `yetkili_adi`    VARCHAR(150)  DEFAULT NULL,
  `telefon`        VARCHAR(20)   DEFAULT NULL,
  `eposta`         VARCHAR(150)  DEFAULT NULL,
  `adres`          TEXT          DEFAULT NULL,
  `ulke`           VARCHAR(80)   NOT NULL DEFAULT 'Türkiye',
  `aktif`          TINYINT(1)    NOT NULL DEFAULT 1,
  `kayit_tarihi`   DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP(),
  PRIMARY KEY (`tedarikci_id`),
  UNIQUE KEY `uq_tedarikci_vergi` (`vergi_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_turkish_ci
  COMMENT='Onaylı tedarikçi listesi';

-- ============================================================
-- 6. MALZEME TABLOSU
-- ============================================================
CREATE TABLE `malzeme` (
  `malzeme_id`    INT(11)       NOT NULL AUTO_INCREMENT,
  `malzeme_adi`   VARCHAR(150)  NOT NULL,
  `kategori`      VARCHAR(80)   DEFAULT NULL,
  `stok_miktari`  INT(11)       NOT NULL DEFAULT 0,
  `kritik_limit`  INT(11)       NOT NULL DEFAULT 10,
  `birim`         VARCHAR(20)   NOT NULL DEFAULT 'Adet',
  `birim_maliyet` DECIMAL(19,4) NOT NULL DEFAULT 0.0000,
  `aktif`         TINYINT(1)    NOT NULL DEFAULT 1,
  PRIMARY KEY (`malzeme_id`),
  UNIQUE KEY `uq_malzeme_adi` (`malzeme_adi`),
  CONSTRAINT `chk_malzeme_stok`    CHECK (`stok_miktari` >= 0),
  CONSTRAINT `chk_kritik_limit`    CHECK (`kritik_limit` >= 0),
  CONSTRAINT `chk_birim_maliyet`   CHECK (`birim_maliyet` >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_turkish_ci;

-- ============================================================
-- 7. STOK HAREKET TABLOSU
-- ============================================================
CREATE TABLE `stok_hareket` (
  `hareket_id`      INT(11)      NOT NULL AUTO_INCREMENT,
  `malzeme_id`      INT(11)      NOT NULL,
  `kullanici_id`    INT(11)      NOT NULL,
  `hareket_turu`    ENUM('GIRIS','CIKIS') NOT NULL,
  `miktar`          INT(11)      NOT NULL,
  `onceki_stok`     INT(11)      NOT NULL DEFAULT 0,
  `sonraki_stok`    INT(11)      NOT NULL DEFAULT 0,
  `hareket_tarihi`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP(),
  `aciklama`        VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`hareket_id`),
  KEY `fk_sh_malzeme`   (`malzeme_id`),
  KEY `fk_sh_kullanici` (`kullanici_id`),
  CONSTRAINT `fk_sh_malzeme`   FOREIGN KEY (`malzeme_id`)   REFERENCES `malzeme`   (`malzeme_id`)   ON UPDATE CASCADE,
  CONSTRAINT `fk_sh_kullanici` FOREIGN KEY (`kullanici_id`) REFERENCES `kullanici` (`kullanici_id`) ON UPDATE CASCADE,
  CONSTRAINT `chk_sh_miktar`   CHECK (`miktar` > 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_turkish_ci;

-- ============================================================
-- 8. SATIN ALMA TALEP TABLOSU
-- ============================================================
CREATE TABLE `satinalma_talep` (
  `talep_id`       INT(11)      NOT NULL AUTO_INCREMENT,
  `departman_id`   INT(11)      NOT NULL,
  `malzeme_id`     INT(11)      NOT NULL,
  `talep_eden_id`  INT(11)      NOT NULL,
  `onaylayan_id`   INT(11)      DEFAULT NULL,
  `talep_miktari`  INT(11)      NOT NULL,
  `durum`          ENUM('BEKLIYOR','ONAYLANDI','REDDEDILDI','TEDARIK_EDILDI') NOT NULL DEFAULT 'BEKLIYOR',
  `red_aciklamasi` VARCHAR(255) DEFAULT NULL,
  `talep_tarihi`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP(),
  `guncelleme`     DATETIME     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(),
  PRIMARY KEY (`talep_id`),
  KEY `fk_st_departman`  (`departman_id`),
  KEY `fk_st_malzeme`    (`malzeme_id`),
  KEY `fk_st_talep_eden` (`talep_eden_id`),
  KEY `fk_st_onaylayan`  (`onaylayan_id`),
  CONSTRAINT `fk_st_departman`  FOREIGN KEY (`departman_id`)  REFERENCES `departman`  (`departman_id`) ON UPDATE CASCADE,
  CONSTRAINT `fk_st_malzeme`    FOREIGN KEY (`malzeme_id`)    REFERENCES `malzeme`    (`malzeme_id`)   ON UPDATE CASCADE,
  CONSTRAINT `fk_st_talep_eden` FOREIGN KEY (`talep_eden_id`) REFERENCES `kullanici`  (`kullanici_id`) ON UPDATE CASCADE,
  CONSTRAINT `fk_st_onaylayan`  FOREIGN KEY (`onaylayan_id`)  REFERENCES `kullanici`  (`kullanici_id`) ON UPDATE CASCADE ON DELETE SET NULL,
  CONSTRAINT `chk_st_miktar`    CHECK (`talep_miktari` > 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_turkish_ci;

-- ============================================================
-- 9. SATIN ALMA SİPARİŞ TABLOSU (YENİ - Hesaplanmış Sütunlu)
-- ============================================================
CREATE TABLE `satin_alma_siparis` (
  `siparis_id`        INT(11)        NOT NULL AUTO_INCREMENT,
  `talep_id`          INT(11)        DEFAULT NULL,
  `tedarikci_id`      INT(11)        NOT NULL,
  `departman_id`      INT(11)        NOT NULL,
  `malzeme_id`        INT(11)        NOT NULL,
  `siparis_miktari`   INT(11)        NOT NULL,
  `birim_fiyat`       DECIMAL(19,4)  NOT NULL DEFAULT 0.0000,
  `kdv_orani`         DECIMAL(5,4)   NOT NULL DEFAULT 0.2000,
  `ara_toplam`        DECIMAL(19,4)  GENERATED ALWAYS AS (`siparis_miktari` * `birim_fiyat`) STORED,
  `kdv_tutari`        DECIMAL(19,4)  GENERATED ALWAYS AS (`siparis_miktari` * `birim_fiyat` * `kdv_orani`) STORED,
  `toplam_tutar`      DECIMAL(19,4)  GENERATED ALWAYS AS (`siparis_miktari` * `birim_fiyat` * (1 + `kdv_orani`)) STORED
                      COMMENT 'Otomatik hesaplanan KDV dahil toplam',
  `durum`             ENUM('HAZIRLANIYOR','GONDERILDI','TESLIM_ALINDI','IPTAL') NOT NULL DEFAULT 'HAZIRLANIYOR',
  `siparis_tarihi`    DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP(),
  `tahmini_teslimat`  DATE           DEFAULT NULL,
  `gercek_teslimat`   DATE           DEFAULT NULL,
  `olusturan_id`      INT(11)        NOT NULL,
  PRIMARY KEY (`siparis_id`),
  KEY `fk_sas_talep`     (`talep_id`),
  KEY `fk_sas_tedarikci` (`tedarikci_id`),
  KEY `fk_sas_departman` (`departman_id`),
  KEY `fk_sas_malzeme`   (`malzeme_id`),
  KEY `fk_sas_olusturan` (`olusturan_id`),
  CONSTRAINT `fk_sas_talep`     FOREIGN KEY (`talep_id`)     REFERENCES `satinalma_talep` (`talep_id`)    ON UPDATE CASCADE ON DELETE SET NULL,
  CONSTRAINT `fk_sas_tedarikci` FOREIGN KEY (`tedarikci_id`) REFERENCES `tedarikci`       (`tedarikci_id`) ON UPDATE CASCADE,
  CONSTRAINT `fk_sas_departman` FOREIGN KEY (`departman_id`) REFERENCES `departman`        (`departman_id`) ON UPDATE CASCADE,
  CONSTRAINT `fk_sas_malzeme`   FOREIGN KEY (`malzeme_id`)   REFERENCES `malzeme`          (`malzeme_id`)   ON UPDATE CASCADE,
  CONSTRAINT `fk_sas_olusturan` FOREIGN KEY (`olusturan_id`) REFERENCES `kullanici`        (`kullanici_id`) ON UPDATE CASCADE,
  CONSTRAINT `chk_sas_miktar`   CHECK (`siparis_miktari` > 0),
  CONSTRAINT `chk_sas_fiyat`    CHECK (`birim_fiyat` >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_turkish_ci
  COMMENT='Satın alma siparişleri - toplam_tutar otomatik hesaplanır';

-- ============================================================
-- 10. MUHASEBE KAYIT TABLOSU
-- ============================================================
CREATE TABLE `muhasebe_kayit` (
  `kayit_id`      INT(11)        NOT NULL AUTO_INCREMENT,
  `departman_id`  INT(11)        NOT NULL,
  `kullanici_id`  INT(11)        NOT NULL,
  `tutar`         DECIMAL(19,4)  NOT NULL,
  `kayit_turu`    ENUM('GELIR','GIDER') NOT NULL,
  `kaynak`        ENUM('BORDRO','SATIN_ALMA','URETIM','DIGER') NOT NULL DEFAULT 'DIGER',
  `referans_id`   INT(11)        DEFAULT NULL,
  `kayit_tarihi`  DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP(),
  `aciklama`      VARCHAR(255)   NOT NULL,
  PRIMARY KEY (`kayit_id`),
  KEY `fk_mk_departman` (`departman_id`),
  KEY `fk_mk_kullanici` (`kullanici_id`),
  CONSTRAINT `fk_mk_departman` FOREIGN KEY (`departman_id`) REFERENCES `departman`  (`departman_id`) ON UPDATE CASCADE,
  CONSTRAINT `fk_mk_kullanici` FOREIGN KEY (`kullanici_id`) REFERENCES `kullanici`  (`kullanici_id`) ON UPDATE CASCADE,
  CONSTRAINT `chk_mk_tutar`    CHECK (`tutar` > 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_turkish_ci;

-- ============================================================
-- 11. BORDRO TABLOSU
-- ============================================================
CREATE TABLE `bordro` (
  `bordro_id`        INT(11)        NOT NULL AUTO_INCREMENT,
  `personel_id`      INT(11)        NOT NULL,
  `donem_yil`        YEAR(4)        NOT NULL,
  `donem_ay`         TINYINT(2)     NOT NULL,
  `brut_maas`        DECIMAL(19,4)  NOT NULL,
  `sgk_kesinti`      DECIMAL(19,4)  NOT NULL,
  `gelir_vergisi`    DECIMAL(19,4)  NOT NULL,
  `net_maas`         DECIMAL(19,4)  NOT NULL,
  `odeme_tarihi`     DATE           DEFAULT NULL,
  `odendi`           TINYINT(1)     NOT NULL DEFAULT 0,
  `olusturma_tarihi` DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP(),
  PRIMARY KEY (`bordro_id`),
  UNIQUE KEY `uq_bordro_donem` (`personel_id`, `donem_yil`, `donem_ay`),
  KEY `fk_bordro_personel` (`personel_id`),
  CONSTRAINT `fk_bordro_personel` FOREIGN KEY (`personel_id`) REFERENCES `personel` (`personel_id`) ON UPDATE CASCADE,
  CONSTRAINT `chk_bordro_ay`      CHECK (`donem_ay` BETWEEN 1 AND 12),
  CONSTRAINT `chk_bordro_maas`    CHECK (`brut_maas` >= 0 AND `net_maas` >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_turkish_ci;

-- ============================================================
-- 12. ÜRETİM PLAN TABLOSU
-- ============================================================
CREATE TABLE `uretim_plan` (
  `plan_id`       INT(11)       NOT NULL AUTO_INCREMENT,
  `departman_id`  INT(11)       NOT NULL,
  `proje_kodu`    VARCHAR(50)   NOT NULL,
  `proje_adi`     VARCHAR(200)  DEFAULT NULL,
  `plan_tarihi`   DATE          NOT NULL,
  `bitis_tarihi`  DATE          DEFAULT NULL,
  `durum`         ENUM('PLANLANDI','DEVAM','TAMAMLANDI','IPTAL') NOT NULL DEFAULT 'PLANLANDI',
  `oncelik`       TINYINT(1)    NOT NULL DEFAULT 2,
  `olusturan_id`  INT(11)       NOT NULL,
  PRIMARY KEY (`plan_id`),
  UNIQUE KEY `uq_proje_kodu` (`proje_kodu`),
  KEY `fk_up_departman` (`departman_id`),
  KEY `fk_up_olusturan` (`olusturan_id`),
  CONSTRAINT `fk_up_departman`  FOREIGN KEY (`departman_id`) REFERENCES `departman` (`departman_id`) ON UPDATE CASCADE,
  CONSTRAINT `fk_up_olusturan`  FOREIGN KEY (`olusturan_id`) REFERENCES `kullanici` (`kullanici_id`) ON UPDATE CASCADE,
  CONSTRAINT `chk_up_oncelik`   CHECK (`oncelik` BETWEEN 1 AND 3)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_turkish_ci;

-- ============================================================
-- 13. EĞİTİM TABLOSU (YENİ)
-- ============================================================
CREATE TABLE `egitim` (
  `egitim_id`      INT(11)      NOT NULL AUTO_INCREMENT,
  `egitim_adi`     VARCHAR(200) NOT NULL,
  `egitim_turu`    ENUM('VIDEO','SINIF','ONLINE','BELGE') NOT NULL DEFAULT 'VIDEO',
  `sure_dakika`    INT(11)      DEFAULT NULL,
  `zorunlu`        TINYINT(1)   NOT NULL DEFAULT 0,
  `video_url`      VARCHAR(500) DEFAULT NULL,
  `aciklama`       TEXT         DEFAULT NULL,
  `aktif`          TINYINT(1)   NOT NULL DEFAULT 1,
  `ekleyen_id`     INT(11)      NOT NULL,
  `ekleme_tarihi`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP(),
  PRIMARY KEY (`egitim_id`),
  KEY `fk_egitim_ekleyen` (`ekleyen_id`),
  CONSTRAINT `fk_egitim_ekleyen` FOREIGN KEY (`ekleyen_id`) REFERENCES `kullanici` (`kullanici_id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_turkish_ci;

-- ============================================================
-- 14. PERSONEL EĞİTİM TABLOSU (YENİ - Pedagojik Takip)
-- ============================================================
CREATE TABLE `personel_egitim` (
  `kayit_id`               INT(11)     NOT NULL AUTO_INCREMENT,
  `personel_id`            INT(11)     NOT NULL,
  `egitim_id`              INT(11)     NOT NULL,
  `atayan_id`              INT(11)     NOT NULL,
  `atama_tarihi`           DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP(),
  `son_tamamlama`          DATETIME    DEFAULT NULL,
  `baslama_tarihi`         DATETIME    DEFAULT NULL,
  `tamamlama_tarihi`       DATETIME    DEFAULT NULL,
  `video_izlendi`          TINYINT(1)  NOT NULL DEFAULT 0 COMMENT 'Video trigger durumu (EgitimServis)',
  `ilerleme_yuzdesi`       TINYINT(3)  NOT NULL DEFAULT 0,
  `durum`                  ENUM('ATANDI','DEVAM','TAMAMLANDI','IPTAL','SURESI_DOLDU') NOT NULL DEFAULT 'ATANDI',
  `hatirlatici_gonderildi` TINYINT(1)  NOT NULL DEFAULT 0,
  `notlar`                 TEXT        DEFAULT NULL,
  PRIMARY KEY (`kayit_id`),
  UNIQUE KEY `uq_personel_egitim` (`personel_id`, `egitim_id`),
  KEY `fk_pe_personel` (`personel_id`),
  KEY `fk_pe_egitim`   (`egitim_id`),
  KEY `fk_pe_atayan`   (`atayan_id`),
  CONSTRAINT `fk_pe_personel`   FOREIGN KEY (`personel_id`) REFERENCES `personel`  (`personel_id`) ON UPDATE CASCADE,
  CONSTRAINT `fk_pe_egitim`     FOREIGN KEY (`egitim_id`)   REFERENCES `egitim`    (`egitim_id`)   ON UPDATE CASCADE,
  CONSTRAINT `fk_pe_atayan`     FOREIGN KEY (`atayan_id`)   REFERENCES `kullanici` (`kullanici_id`) ON UPDATE CASCADE,
  CONSTRAINT `chk_pe_ilerleme`  CHECK (`ilerleme_yuzdesi` BETWEEN 0 AND 100)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_turkish_ci
  COMMENT='EgitimServis pedagojik takip tablosu';

-- ============================================================
-- 15. SYSTEM LOGS TABLOSU (YENİ - IT Audit Logging)
-- ============================================================
CREATE TABLE `system_logs` (
  `log_id`         BIGINT(20)   NOT NULL AUTO_INCREMENT,
  `kullanici_id`   INT(11)      DEFAULT NULL,
  `islem_turu`     VARCHAR(50)  NOT NULL COMMENT 'LOGIN, LOGOUT, CREATE, UPDATE, DELETE, ACCESS_DENIED',
  `modul`          VARCHAR(80)  NOT NULL,
  `tablo_adi`      VARCHAR(80)  DEFAULT NULL,
  `kayit_id`       INT(11)      DEFAULT NULL,
  `eski_deger`     JSON         DEFAULT NULL,
  `yeni_deger`     JSON         DEFAULT NULL,
  `ip_adresi`      VARCHAR(45)  DEFAULT NULL,
  `basarili`       TINYINT(1)   NOT NULL DEFAULT 1,
  `hata_mesaji`    TEXT         DEFAULT NULL,
  `log_tarihi`     DATETIME(3)  NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT 'Milisaniye hassasiyeti',
  PRIMARY KEY (`log_id`),
  KEY `idx_log_kullanici` (`kullanici_id`),
  KEY `idx_log_tarih`     (`log_tarihi`),
  KEY `idx_log_islem`     (`islem_turu`),
  CONSTRAINT `fk_log_kullanici` FOREIGN KEY (`kullanici_id`) REFERENCES `kullanici` (`kullanici_id`) ON UPDATE CASCADE ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_turkish_ci
  COMMENT='IT Audit Log - ErisimKontrolServisi çıktısı';

-- ============================================================
-- VIEW 1: v_kritik_stok
-- ============================================================
CREATE OR REPLACE VIEW `v_kritik_stok` AS
SELECT
  m.malzeme_id,
  m.malzeme_adi,
  m.kategori,
  m.stok_miktari,
  m.kritik_limit,
  m.birim,
  m.birim_maliyet,
  (m.kritik_limit - m.stok_miktari) AS eksik_miktar,
  ROUND((m.kritik_limit - m.stok_miktari) * m.birim_maliyet, 4) AS tahmini_maliyet,
  CASE
    WHEN m.stok_miktari = 0                              THEN 'KRITIK'
    WHEN m.stok_miktari < m.kritik_limit * 0.5          THEN 'DUSUK'
    ELSE 'UYARI'
  END AS durum_seviyesi
FROM `malzeme` m
WHERE m.stok_miktari < m.kritik_limit
  AND m.aktif = 1
ORDER BY eksik_miktar DESC;

-- ============================================================
-- VIEW 2: v_satin_alma_ozet
-- ============================================================
CREATE OR REPLACE VIEW `v_satin_alma_ozet` AS
SELECT
  st.talep_id,
  d.departman_adi,
  m.malzeme_adi,
  m.birim,
  st.talep_miktari,
  st.durum           AS talep_durum,
  st.talep_tarihi,
  CONCAT(k_talep.adi, ' ', k_talep.soyadi) AS talep_eden,
  CONCAT(k_onay.adi,  ' ', k_onay.soyadi)  AS onaylayan,
  sas.siparis_id,
  t.firma_adi        AS tedarikci,
  sas.birim_fiyat,
  sas.ara_toplam,
  sas.kdv_tutari,
  sas.toplam_tutar,
  sas.durum          AS siparis_durum,
  sas.siparis_tarihi,
  sas.tahmini_teslimat,
  sas.gercek_teslimat
FROM `satinalma_talep` st
  JOIN  `departman`          d       ON d.departman_id         = st.departman_id
  JOIN  `malzeme`            m       ON m.malzeme_id           = st.malzeme_id
  JOIN  `kullanici`          k_talep ON k_talep.kullanici_id   = st.talep_eden_id
  LEFT JOIN `kullanici`          k_onay  ON k_onay.kullanici_id    = st.onaylayan_id
  LEFT JOIN `satin_alma_siparis` sas     ON sas.talep_id           = st.talep_id
  LEFT JOIN `tedarikci`          t       ON t.tedarikci_id         = sas.tedarikci_id
ORDER BY st.talep_tarihi DESC;

-- ============================================================
-- VIEW 3: v_personel_egitim_durumu (Bonus)
-- ============================================================
CREATE OR REPLACE VIEW `v_personel_egitim_durumu` AS
SELECT
  p.personel_id,
  CONCAT(p.adi, ' ', p.soyadi) AS personel_adi,
  d.departman_adi,
  e.egitim_adi,
  e.egitim_turu,
  e.zorunlu,
  pe.durum,
  pe.ilerleme_yuzdesi,
  pe.video_izlendi,
  pe.atama_tarihi,
  pe.son_tamamlama,
  pe.tamamlama_tarihi,
  CASE
    WHEN pe.durum = 'TAMAMLANDI'                                          THEN 'TAMAM'
    WHEN pe.son_tamamlama < NOW() AND pe.durum != 'TAMAMLANDI'           THEN 'GECIKMIS'
    WHEN pe.son_tamamlama < DATE_ADD(NOW(), INTERVAL 3 DAY)              THEN 'YAKLISIYOR'
    ELSE 'NORMAL'
  END AS uyari_durumu
FROM `personel_egitim` pe
  JOIN `personel`  p ON p.personel_id  = pe.personel_id
  JOIN `egitim`    e ON e.egitim_id    = pe.egitim_id
  JOIN `departman` d ON d.departman_id = p.departman_id
ORDER BY uyari_durumu, pe.son_tamamlama;

-- ============================================================
SET FOREIGN_KEY_CHECKS = 1;
COMMIT;
-- Kurulum tamamlandı. Toplam: 15 tablo + 3 view
-- Doğrulama: SELECT TABLE_NAME FROM information_schema.TABLES WHERE TABLE_SCHEMA = 'fabrika_erp';
