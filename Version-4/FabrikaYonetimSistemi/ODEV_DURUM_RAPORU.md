# Fabrika ERP Simulasyonu - Odev Durum Raporu

## Proje Amaci
Bu projenin amaci, fabrikanin IK, Uretim, Depo, BT, Satin Alma ve Finans departmanlarini OOP prensipleriyle tek bir ERP simulasyonunda toplamaktir. Proje su an terminal tabanli calismakta olup JavaFX asamasi sonraki faz olarak planlanmistir.

## Mevcut Durum (23.03.2026)
- Calisan terminal uygulamasi mevcut (`com.fabrika.Main`).
- Role-based menu ve departman bazli yetki akisi aktif.
- `Department` taban sinifi uzerinden 6 departman kalitim yapiyor.
- Departmanlara ozel surec simulasyonlari calisiyor.
- Interface entegrasyonu aktif (`SurecYurutulebilir`).
- Dosya islemleri aktif (`DosyaKayitUtil`) ve `logs/islem-kaydi.txt` uzerine kayit aliniyor.
- MySQL icin tam veritabani semasi ve seed script eklendi (`database/schema_mysql.sql`).
- Generic yapi eklendi: `BaseEntity` + `BaseRepository<T extends BaseEntity>`.

## Mimari ve Teknik Yapi
- Katmanli yapi: `model`, `repository`, `service`, `ui`, `util`, `config`.
- OOP prensipleri: encapsulation, inheritance, polymorphism, interface-based tasarim.
- Collections Framework: role matrisi icin `LinkedHashMap<Integer, Department>`.
- Dosya yonetimi: Java NIO (`Files`, `Path`, `StandardOpenOption`).

## Hafta Hafta Ilerleme Ozeti
### 1. Hafta
- Problem analizi ve ERP kapsam tanimi yapildi.
- N-tier paket yapisi olusturuldu.
- Temel siniflar icin iskelet proje kuruldu.

### 2. Hafta
- `Department` ust sinifi yazildi.
- 6 alt departman sinifi olusturuldu:
  - `HumanResources`, `Uretim`, `Depo`, `It`, `SatinAlma`, `Finans`
- Ortak davranislar (durum gosterimi, islem akisi) miras ile birlestirildi.

### 3. Hafta
- Departman bazli surec metotlari uygulandi.
- IK, Uretim, Depo, BT, Satin Alma ve Finans icin islevsel akislar tamamlandi.

### 4. Hafta
- `Scanner` tabanli dinamik terminal menusu tamamlandi.
- Rol secimi ve departmanlar arasi gecis akisi duzenlendi.

### 5. Hafta
- Yetki matrisi yapisi guclendirildi.
- `SurecYurutulebilir` interface'i ile surec calistirma davranisi standartlastirildi.
- Loglama alt yapisi eklendi ve dosyaya kalici kayit alinmaya baslandi.

### 6. Hafta - Veritabani Tasarimi
- ERP sureclerine uygun ER modeli netlestirildi.
- Cekirdek tablolar tasarlandi:
  - `rol`, `kullanici`, `departman`, `personel`, `malzeme`
  - `stok_hareket`, `uretim_plan`, `satinalma_talep`, `muhasebe_kayit`
- Primary key, foreign key, check ve index tasarimlari eklendi.
- Generic service tanımlandı.

### 7. Hafta - SQL Script Yazimi
- MySQL 8+ ile uyumlu tam SQL script hazirlandi.
- `CREATE DATABASE`, `CREATE TABLE`, `INDEX`, `INSERT` (seed) bolumleri eklendi.
- Kurulum dokumani olusturuldu:
  - `database/KURULUM_TALIMATI.md`

## Generic Yapi Entegrasyonu
- `BaseEntity` sinifi ile tum entity'ler icin ortak `id` alani tanimlandi.
- `BaseRepository<T extends BaseEntity>` ile generic CRUD iskeleti eklendi.
- Uygulanan repository siniflari:
  - `PersonelRepository extends BaseRepository<Personel>`
  - `MalzemeRepository extends BaseRepository<Malzeme>`
  - `IslemRepository extends BaseRepository<Islem>`
- Bu entegrasyon mevcut terminal akisina zarar vermeden eklendi.

## Kullanilan Kutuphaneler ve Teknolojiler
- Java SE
- `java.util` (Scanner, Map, LinkedHashMap, List, Optional)
- `java.time` (LocalDateTime, DateTimeFormatter)
- `java.nio.file` (Files, Path, StandardOpenOption)
- MySQL 8+
- MySQL Workbench

## Yasanan Zorluklar ve Cozumler
- Konsol menude hatali girisler: `sayiOku` ile guvenli sayisal dogrulama eklendi.
- Surec davranislarinda standardizasyon: interface tabanli sozlesme ile cozuldu.
- Log kayitlarinda klasor/dosya eksikligi: otomatik klasor olusturma ve append yazim eklendi.
- Veritabani tarafinda butunluk: FK ve CHECK kisitlari ile veri kalitesi guclendirildi.

## Sonraki Asama (Bekleyen)
- JavaFX UI katmaninin devreye alinmasi.
- `DatabaseConnection` sinifinin aktif JDBC baglantisi ile tamamlanmasi.
- Repository katmaninin in-memory generic yapi yerine SQL tabanli calisacak sekilde genisletilmesi.
