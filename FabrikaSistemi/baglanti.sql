-- 6-7. Hafta: Veritabani Tasarimi ve SQL Scripti
-- Hedef DB: MySQL 8+

DROP DATABASE IF EXISTS fabrika_erp;
CREATE DATABASE fabrika_erp CHARACTER SET utf8mb4 COLLATE utf8mb4_turkish_ci;
USE fabrika_erp;

CREATE TABLE rol (
    rol_id INT AUTO_INCREMENT PRIMARY KEY,
    rol_adi VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE kullanici (
    kullanici_id INT AUTO_INCREMENT PRIMARY KEY,
    adi VARCHAR(100) NOT NULL,
    soyadi VARCHAR(100) NOT NULL,
    sifre_hash VARCHAR(255) NOT NULL,
    rol_id INT NOT NULL,
    aktif TINYINT(1) NOT NULL DEFAULT 1,
    kayit_tarihi DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_kullanici_rol FOREIGN KEY (rol_id) REFERENCES rol(rol_id)
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
    adi VARCHAR(100) NOT NULL,
    soyadi VARCHAR(100) NOT NULL,
    maas DECIMAL(12,2) NOT NULL DEFAULT 0,
    ise_giris_tarihi DATE NOT NULL,
    calisma_baslangic DATETIME NOT NULL,
    calisma_bitis DATETIME NOT NULL,
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
    CONSTRAINT FOREIGN KEY (departman_id) REFERENCES departman(departman_id)
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

INSERT INTO rol (rol_adi) VALUES
('IK'), ('URETIM'), ('DEPO'), ('BT'), ('SATIN_ALMA'), ('FINANS'), ('MUDUR');

INSERT INTO kullanici (ad_soyad, kullanici_adi, sifre_hash, rol_id) VALUES
('Ahmet Yilmaz', 'ahmet.ik', 'ornek_hash_ik', 1),
('Mehmet Kaya', 'mehmet.uretim', 'ornek_hash_uretim', 2),
('Ali Riza', 'ali.depo', 'ornek_hash_depo', 3),
('Ela Eda', 'ela.bt', 'ornek_hash_bt', 4),
('Gizem Sen', 'gizem.satinalma', 'ornek_hash_satinalma', 5),
('Aylin Tuna', 'aylin.finans', 'ornek_hash_finans', 6);

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
('Bakir Kablo', 800, 250, 'METRE');

INSERT INTO personel (departman_id, ad_soyad, maas, ise_giris_tarihi, aktif) VALUES
(1, 'Veli Demir', 32000.00, '2025-01-10', 1),
(2, 'Burak Usta', 38000.00, '2024-11-03', 1),
(3, 'Deniz Yildiz', 30000.00, '2025-02-20', 1);

-- Ornek sorgular
-- SELECT d.departman_adi, COUNT(p.personel_id) personel_sayisi
-- FROM departman d LEFT JOIN personel p ON p.departman_id = d.departman_id
-- GROUP BY d.departman_id;

-- SELECT m.malzeme_adi, m.stok_miktari, m.kritik_limit
-- FROM malzeme m WHERE m.stok_miktari <= m.kritik_limit;

--ALTER TABLE departman 
--ADD CONSTRAINT fk_departman_kullanici 
--FOREIGN KEY (sorumlu_kullanici_id) REFERENCES kullanici(kullanici_id);

--ALTER TABLE stok ADD CONSTRAINT chk_stok_miktari CHECK (stok_miktari >= 0);
--ALTER TABLE stok ADD CONSTRAINT chk_kritik_limit CHECK (kritik_limit >= 0);