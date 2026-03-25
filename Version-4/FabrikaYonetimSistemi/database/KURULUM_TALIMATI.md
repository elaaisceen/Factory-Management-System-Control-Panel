# MySQL Kurulum ve Calistirma Talimati

## 1) Gerekli araclar
- MySQL Community Server 8+
- MySQL Workbench

## 2) Veritabanini olustur
1. MySQL Workbench ac.
2. Sunucuna baglan (Local instance).
3. `database/schema_mysql.sql` dosyasini ac.
4. Tum scripti calistir (yildirim ikonu).
5. Sol panelde `fabrika_erp` veritabaninin olustugunu dogrula.

## 3) Hizli kontrol sorgulari
```sql
USE fabrika_erp;
SHOW TABLES;
SELECT * FROM departman;
SELECT * FROM rol;
```

## 4) Java tarafi baglanti notu
- JDBC URL: `jdbc:mysql://localhost:3306/fabrika_erp?useSSL=false&serverTimezone=Europe/Istanbul`
- Kullanici: `root` (veya kendi olusturdugun kullanici)
- Sifre: kurulumda verdigin sifre

## 5) Maven/Gradle yoksa JAR ekleme
- `mysql-connector-j` jar dosyasini projeye ekleyip classpath'e dahil et.

## 6) Guvenli kullanim notu
- Gercek projede `sifre_hash` alanina duz metin degil hash yaz.
- Uygulama kodunda SQL icin `PreparedStatement` kullan.
