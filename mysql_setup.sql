-- Uygulama icin MySQL kullanicisi olusturma (XAMPP / MySQL 8+ uyumlu)
-- Not: Bu dosyayi phpMyAdmin SQL sekmesinde veya mysql client'ta calistir.

-- 1) DB yoksa olustur
CREATE DATABASE IF NOT EXISTS fabrika_erp
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_turkish_ci;

-- 2) Uygulama kullanicisi (onerilen)
-- Sifreyi istersen degistir (ornegin: factory123!)
CREATE USER IF NOT EXISTS 'factory_app'@'localhost' IDENTIFIED BY 'factory123!';

-- 3) Yetkiler
GRANT ALL PRIVILEGES ON fabrika_erp.* TO 'factory_app'@'localhost';
FLUSH PRIVILEGES;

-- 4) (Opsiyonel) Root sifresiz giris kapaliysa, root ile test etmek yerine bu kullaniciyi kullan.

