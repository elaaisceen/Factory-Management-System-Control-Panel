CREATE DATABASE IF NOT EXISTS fabrika_erp
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_turkish_ci;

USE fabrika_erp;

CREATE TABLE IF NOT EXISTS departman (
  departman_id INT AUTO_INCREMENT PRIMARY KEY,
  kod VARCHAR(30) NOT NULL UNIQUE,
  ad VARCHAR(120) NOT NULL,
  sorumlu_personel VARCHAR(120) NOT NULL,
  calisan_sayisi INT NOT NULL DEFAULT 0,
  aylik_butce DECIMAL(14,2) NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS personel (
  personel_id INT AUTO_INCREMENT PRIMARY KEY,
  ad_soyad VARCHAR(120) NOT NULL,
  departman_id INT NOT NULL,
  gorev VARCHAR(100),
  maas DECIMAL(12,2) DEFAULT 0,
  durum VARCHAR(30) DEFAULT 'AKTIF',
  ise_giris_tarihi DATE,
  CONSTRAINT fk_personel_departman FOREIGN KEY (departman_id) REFERENCES departman(departman_id)
);

CREATE TABLE IF NOT EXISTS malzeme (
  malzeme_id INT AUTO_INCREMENT PRIMARY KEY,
  ad VARCHAR(150) NOT NULL,
  stok_kodu VARCHAR(60) UNIQUE,
  mevcut_stok INT NOT NULL DEFAULT 0,
  kritik_seviye INT NOT NULL DEFAULT 0,
  birim VARCHAR(20) DEFAULT 'adet'
);

CREATE TABLE IF NOT EXISTS tedarikci (
  tedarikci_id INT AUTO_INCREMENT PRIMARY KEY,
  firma_adi VARCHAR(150) NOT NULL,
  yetkili_adi VARCHAR(100),
  telefon VARCHAR(30),
  eposta VARCHAR(120)
);

CREATE TABLE IF NOT EXISTS siparis (
  siparis_id INT AUTO_INCREMENT PRIMARY KEY,
  tedarikci_id INT NOT NULL,
  malzeme_id INT NOT NULL,
  miktar INT NOT NULL,
  durum VARCHAR(30) NOT NULL DEFAULT 'PENDING',
  siparis_tarihi DATE NOT NULL,
  teslim_tarihi DATE,
  CONSTRAINT fk_siparis_tedarikci FOREIGN KEY (tedarikci_id) REFERENCES tedarikci(tedarikci_id),
  CONSTRAINT fk_siparis_malzeme FOREIGN KEY (malzeme_id) REFERENCES malzeme(malzeme_id)
);

CREATE TABLE IF NOT EXISTS uretim_projesi (
  proje_id INT AUTO_INCREMENT PRIMARY KEY,
  ad VARCHAR(150) NOT NULL,
  baslangic_tarihi DATE NOT NULL,
  bitis_tarihi DATE,
  durum VARCHAR(30) NOT NULL DEFAULT 'PLANLANDI'
);

CREATE TABLE IF NOT EXISTS bordro (
  bordro_id INT AUTO_INCREMENT PRIMARY KEY,
  personel_ad VARCHAR(120) NOT NULL,
  donem_ay_yil VARCHAR(7),
  baz_maas DECIMAL(12,2),
  calisilan_gun INT,
  mesai_saati INT,
  mesai_ucreti DECIMAL(12,2),
  yan_haklar_toplami DECIMAL(12,2),
  kesintiler DECIMAL(12,2),
  net_odenen DECIMAL(12,2),
  durum VARCHAR(30),
  olusturma_tarihi DATETIME
);

INSERT INTO departman (kod, ad, sorumlu_personel, calisan_sayisi, aylik_butce) VALUES
('IK', 'Insan Kaynaklari', 'Ahmet Y.', 12, 350000),
('URETIM', 'Uretim', 'Mehmet K.', 46, 1250000),
('DEPO', 'Depo', 'Ali Riza', 18, 420000),
('BT', 'Bilgi Teknolojileri', 'Ela Eda', 9, 280000),
('SATIN_ALMA', 'Satin Alma', 'Gizem S.', 7, 300000),
('FINANS', 'Finans', 'Aylin T.', 11, 500000)
ON DUPLICATE KEY UPDATE ad = VALUES(ad);
