@echo off
title Fabrika ERP - Masaüstü Başlatıcı
cd /d "%~dp0"

echo ======================================================
echo    PULSE INDUSTRIAL - SİSTEM BAŞLATILIYOR
echo ======================================================

:: 1. Derleme (Her ihtimale karşı)
echo [*] Derleniyor...
call mvn clean compile -DskipTests

if errorlevel 1 (
    echo [HATA] Derleme başarısız. Lütfen interneti ve Maven'ı kontrol edin.
    pause
    exit /b 1
)

:: 2. Uygulamayı Başlat (En Garanti Yöntem)
echo [*] Uygulama açılıyor...
:: Maven'ın tüm bağımlılıkları ve modülleri dahil etmesini sağlayan exec komutu
call mvn exec:java -Dexec.mainClass="com.factory.stitch.Main"

if errorlevel 1 (
    echo.
    echo [HATA] Uygulama başlatılamadı. 
    echo İpucu: MySQL servisinin açık olduğundan emin olun.
    pause
)
