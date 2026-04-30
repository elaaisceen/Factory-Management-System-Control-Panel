# 🏭 Fabrika Yönetim Sistemi - Enterprise ERP

**Sürüm:** 2.0  
**Durum:** ✅ Sunuma Hazır  
**Son Güncelleme:** 30 Nisan 2026

---

## 📋 Proje Özeti

Fabrika Yönetim Sistemi (Factory Management System Control Panel), departman, personel, stok, üretim, satın alma ve finans süreçlerini tek bir web panelinde yönetmek için geliştirilmiş kapsamlı bir Enterprise ERP uygulamasıdır.

### Temel Özellikler
- ✅ **Kalıcı Veri Depolama:** MySQL veritabanı ile tam entegrasyon
- ✅ **REST API:** Modern Spring Boot backend
- ✅ **Modern Frontend:** Tailwind CSS + Vanilla JavaScript
- ✅ **Rol Bazlı Yetkilendirme:** Departman bazlı erişim kontrolü
- ✅ **Gerçek Zamanlı Dashboard:** 7 farklı departman dashboard'u
- ✅ **Türkçe Arayüz:** Tamamen Türkçe dokümantasyon ve UI

---

## 🏗️ Mimari Yapı

### Teknoloji Stack

| Katman | Teknoloji | Versiyon |
|--------|-----------|----------|
| **Backend** | Spring Boot | 3.3.0 |
| **Java** | JDK | 21.0.8 |
| **Veritabanı** | MySQL | 8.0+ |
| **ORM** | Hibernate | 6.5.2 |
| **Build Tool** | Maven | 3.3.1 |
| **Web Server** | Tomcat | 10.1.24 |
| **Frontend** | Vanilla JavaScript | ES6+ |
| **CSS Framework** | Tailwind CSS | 3.x |
| **Icons** | Material Symbols | Outlined |
| **Fonts** | Manrope, Inter | Latest |

### Proje Yapısı

```
Factory-Management-System-Control-Panel-main/
├── FabrikaSistemi/                    # Ana Proje Klasörü
│   ├── backend_service/              # Spring Boot Backend
│   │   ├── src/main/java/com/factory/stitch/
│   │   │   ├── controller/           # REST API Controller'ları (12)
│   │   │   ├── service/              # Business Logic Layer (10)
│   │   │   ├── repository/           # JPA Repository'ler (10)
│   │   │   ├── model/                # Entity Sınıfları (23)
│   │   │   ├── dto/                  # Data Transfer Objects (6)
│   │   │   ├── config/               # Konfigürasyon Sınıfları
│   │   │   ├── dao/                  # Custom DAO'lar
│   │   │   ├── ui/                   # JavaFX UI Bileşenleri
│   │   │   └── util/                 # Yardımcı Sınıflar
│   │   ├── src/main/resources/
│   │   │   └── application.properties # MySQL Bağlantı Ayarları
│   │   └── pom.xml                   # Maven Dependencies
│   │
│   ├── hr_dashboard/                 # İnsan Kaynakları Dashboard
│   ├── stock_dashboard/              # Stok/Depo Dashboard
│   ├── production_dashboard/         # Üretim Dashboard
│   ├── purchasing_dashboard/         # Satın Alma Dashboard
│   ├── finance_dashboard/            # Finans Dashboard
│   ├── it_dashboard/                 # IT Destek Dashboard
│   ├── maintenance_dashboard/        # Bakım Dashboard
│   ├── login_register/               # Kimlik Doğrulama
│   ├── assets/                       # Ortak Kaynaklar
│   ├── index.html                    # Ana Landing Page
│   ├── baglanti.sql                  # Veritabanı Şeması
│   ├── baslat.bat                    # Başlatma Scripti
│   └── start-desktop-app.bat         # Desktop Başlatma Scripti
│
├── MYSQL-WORKBENCH-KURULUM.md        # MySQL Kurulum Rehberi
├── YONETICI-KURULUMU.md              # Yönetici Kurulum Rehberi
├── UYGULAMA-BASLATMA.md              # Uygulama Başlatma Rehberi
└── README.MD                         # Bu Dosya
```

---

## 🚀 Kurulum ve Başlatma

### Ön Gereksinimler

1. **Java JDK 21+** - [İndir](https://www.oracle.com/java/technologies/downloads/)
2. **Maven 3.3+** - [İndir](https://maven.apache.org/download.cgi)
3. **MySQL 8.0+** - [İndir](https://dev.mysql.com/downloads/mysql/)
4. **MySQL Workbench** (Opsiyonel) - [İndir](https://dev.mysql.com/downloads/workbench/)

### Adım 1: Veritabanı Kurulumu

```bash
# MySQL Workbench'i açın
# FabrikaSistemi/baglanti.sql dosyasını import edin
# Veritabanı adı: fabrika_erp
# Port: 3307
```

### Adım 2: Backend Konfigürasyonu

`backend_service/src/main/resources/application.properties` dosyasını düzenleyin:

```properties
# MySQL Bağlantı Ayarları
spring.datasource.url=jdbc:mysql://localhost:3307/fabrika_erp?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA/Hibernate Ayarları
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect

# Server Ayarları
server.port=8080
```

### Adım 3: Uygulamayı Başlatma

**Windows için:**
```batch
# Seçenek 1: Ana klasörden
start-desktop-app.bat

# Seçenek 2: FabrikaSistemi klasöründen
baslat.bat
```

**Manuel Başlatma:**
```bash
cd FabrikaSistemi/backend_service
mvn package -DskipTests -q
cd ..
java -jar backend_service\target\stitch-0.0.1-SNAPSHOT.jar --desktop
```

### Adım 4: Tarayıcıdan Erişim

```
http://localhost:8080/index.html
```

---

## 🌐 API Endpoint'leri

### Authentication

| Endpoint | Method | Açıklama |
|----------|--------|----------|
| `/api/auth/login` | POST | Kullanıcı girişi |
| `/api/auth/kayit` | POST | Yeni kullanıcı kaydı |
| `/api/auth/logout` | GET | Çıkış işlemi |

### Personel (HR)

| Endpoint | Method | Açıklama |
|----------|--------|----------|
| `/api/personel` | GET | Tüm personeli listele |
| `/api/personel/{id}` | GET | ID'ye göre personel getir |
| `/api/personel/departman/{departmanId}` | GET | Departman personeli |
| `/api/personel` | POST | Yeni personel ekle |
| `/api/personel/{id}` | PUT | Personel güncelle |
| `/api/personel/{id}` | DELETE | Personel sil |

### Stok (Malzeme)

| Endpoint | Method | Açıklama |
|----------|--------|----------|
| `/api/malzeme` | GET | Tüm malzemeleri listele |
| `/api/malzeme/{id}` | GET | ID'ye göre malzeme getir |
| `/api/malzeme` | POST | Yeni malzeme ekle |
| `/api/malzeme/{id}` | PUT | Malzeme güncelle |
| `/api/malzeme/{id}` | DELETE | Malzeme sil |

### Üretim Planı

| Endpoint | Method | Açıklama |
|----------|--------|----------|
| `/api/uretim-plan` | GET | Tüm üretim planlarını listele |
| `/api/uretim-plan/{id}` | GET | ID'ye göre plan getir |
| `/api/uretim-plan` | POST | Yeni üretim planı ekle |
| `/api/uretim-plan/{id}` | PUT | Plan güncelle |
| `/api/uretim-plan/{id}` | DELETE | Plan sil |

### Satın Alma

| Endpoint | Method | Açıklama |
|----------|--------|----------|
| `/api/satin-alma-talep` | GET | Tüm satın alma talepleri |
| `/api/satin-alma-talep/{id}` | GET | ID'ye göre talep getir |
| `/api/satin-alma-talep` | POST | Yeni satın alma talebi |
| `/api/satin-alma-talep/{id}` | PUT | Talep güncelle |
| `/api/satin-alma-talep/{id}` | DELETE | Talep sil |

### Finans (Muhasebe)

| Endpoint | Method | Açıklama |
|----------|--------|----------|
| `/api/muhasebe` | GET | Tüm muhasebe kayıtları |
| `/api/muhasebe/{id}` | GET | ID'ye göre kayıt getir |
| `/api/muhasebe` | POST | Yeni muhasebe kaydı |
| `/api/muhasebe/{id}` | PUT | Kayıt güncelle |
| `/api/muhasebe/{id}` | DELETE | Kayıt sil |

### ERP Genel

| Endpoint | Method | Açıklama |
|----------|--------|----------|
| `/api/erp/health` | GET | Backend sağlık kontrolü |
| `/api/erp/departments?role={role}` | GET | Departman listesi |
| `/api/erp/departments/{code}?role={role}` | GET | Departman durumu |
| `/api/erp/departments/{code}/process?role={role}` | POST | Departman süreci çalıştır |

---

## 🎨 Dashboard'lar

### 1️⃣ İnsan Kaynakları (HR Dashboard)

**URL:** `http://localhost:8080/hr_dashboard/personel.html`

**Özellikler:**
- Personel ekleme/düzenleme/silme
- Departman bazlı filtreleme
- Aktif/pasif durum yönetimi
- Maaş bilgileri
- Çalışma saatleri takibi

**API Entegrasyonu:** ✅ `/api/personel`

---

### 2️⃣ Stok Yönetimi (Stock Dashboard)

**URL:** `http://localhost:8080/stock_dashboard/stock.html`

**Özellikler:**
- Malzeme ekleme/düzenleme
- Stok miktarı takibi
- Kritik stok uyarıları
- Barkod okuma desteği
- Birim yönetimi (ADET, KG, METRE)

**API Entegrasyonu:** ✅ `/api/malzeme`

---

### 3️⃣ Üretim Planlama (Production Dashboard)

**URL:** `http://localhost:8080/production_dashboard/production.html`

**Özellikler:**
- Üretim planı oluşturma
- Proje kodu yönetimi
- Durum takibi (PLANLANDI, DEVAM, TAMAMLANDI)
- Departman bazlı üretim
- Tarih bazlı planlama

**API Entegrasyonu:** ✅ `/api/uretim-plan`

---

### 4️⃣ Satın Alma (Purchasing Dashboard)

**URL:** `http://localhost:8080/purchasing_dashboard/purchasing.html`

**Özellikler:**
- Satın alma talebi oluşturma
- Tedarikçi yönetimi
- Onay süreci
- CSV export desteği
- Departman bazlı talepler

**API Entegrasyonu:** ✅ `/api/satin-alma-talep`

---

### 5️⃣ Finans/Muhasebe (Finance Dashboard)

**URL:** `http://localhost:8080/finance_dashboard/finance.html`

**Özellikler:**
- Muhasebe kayıtları
- Gelir/gider takibi
- Bütçe yönetimi
- Raporlama
- Excel export

**API Entegrasyonu:** ✅ `/api/muhasebe`

---

### 6️⃣ IT Destek (IT Dashboard)

**URL:** `http://localhost:8080/it_dashboard/it.html`

**Özellikler:**
- Destek talepleri
- Sorun takibi
- Kullanıcı yönetimi
- Sistem logları

**API Entegrasyonu:** ⏳ (Devam ediyor)

---

### 7️⃣ Bakım (Maintenance Dashboard)

**URL:** `http://localhost:8080/maintenance_dashboard/maintenance.html`

**Özellikler:**
- Bakım planları
- Ekipman takibi
- Periyodik bakım
- Sorun raporları

**API Entegrasyonu:** ⏳ (Devam ediyor)

---

## 💾 Veritabanı Şeması

### Tablolar

| Tablo Adı | Açıklama |
|-----------|----------|
| `personel` | Personel bilgileri |
| `malzeme` | Malzeme/stok bilgileri |
| `uretim_plan` | Üretim planları |
| `satinalma_talep` | Satın alma talepleri |
| `muhasebe_kayit` | Muhasebe kayıtları |
| `stok_hareket` | Stok hareketleri |
| `departman` | Departman bilgileri |
| `kullanici` | Kullanıcı hesapları |
| `rol` | Rol ve yetki matrisi |
| `bordro` | Maaş bordroları |

### ID Tipi

**Tüm tablolarda:** `INT` (MySQL Integer)  
**Java Entity'lerde:** `Integer` (Long kullanılmıyor - MySQL uyumluluğu için)

---

## 🔧 Yapılan Düzeltmeler ve İyileştirmeler

### Backend Düzeltmeleri

1. **ID Tipi Düzeltmesi**
   - ❌ Önce: `Long id` (MySQL INT ile uyumsuz)
   - ✅ Şimdi: `Integer id` (tam uyumluluk)
   - Etkilen Dosyalar: Tüm Entity'ler, Repository'ler, Controller'lar

2. **Veritabanı Bağlantısı**
   - ❌ Önce: H2 In-Memory DB (veriler kayboluyordu)
   - ✅ Şimdi: MySQL 8.0 (kalıcı depolama)
   - Dosya: `application.properties`, `pom.xml`

3. **H2 Dependency Kaldırıldı**
   - `pom.xml`'den H2 dependency kaldırıldı
   - MySQL zorunlu hale getirildi

### Frontend Düzeltmeleri

1. **localStorage → API Geçişi**
   - ❌ Önce: localStorage (tarayıcıda geçici)
   - ✅ Şimdi: Fetch API + Backend (kalıcı MySQL)
   - Düzenlenen Dosyalar:
     - `hr_dashboard/personel.html`
     - `stock_dashboard/stock.html`
     - `production_dashboard/production.js`
     - `purchasing_dashboard/purchasing.html`

2. **Türkçe Açıklamalar Eklendi**
   - Backend Entity'ler: Detaylı Javadoc
   - Controller'lar: Endpoint açıklamaları
   - Frontend: Fonksiyon yorumları

### Script Düzeltmeleri

1. **baslat.bat** - Temizlendi
2. **start-desktop-app.bat** - Temizlendi

---

## 📊 İstatistikler

### Kod İstatistikleri

| Kategori | Sayı |
|----------|------|
| Java Dosyaları | 50+ |
| Entity Sınıfları | 23 |
| Controller Sınıfları | 12 |
| Service Sınıfları | 10 |
| Repository Sınıfları | 10 |
| HTML Dosyaları | 25+ |
| JavaScript Dosyaları | 30+ |
| SQL Dosyaları | 2 |

### Dashboard İstatistikleri

| Departman | Durum | API Entegrasyonu |
|-----------|-------|-----------------|
| HR | ✅ Çalışıyor | ✅ Tamam |
| Stok | ✅ Çalışıyor | ✅ Tamam |
| Üretim | ✅ Çalışıyor | ✅ Tamam |
| Satın Alma | ✅ Çalışıyor | ✅ Tamam |
| Finans | ✅ Çalışıyor | ✅ Tamam |
| IT | ✅ Çalışıyor | ⏳ Devam |
| Bakım | ✅ Çalışıyor | ⏳ Devam |

---

## 🐛 Sorun Giderme

### Yaygın Sorunlar

**1. Veritabanı Bağlantı Hatası**
```
Hata: Communications link failure
Çözüm: MySQL'in çalıştığını ve port 3307 açık olduğunu kontrol et
```

**2. Maven Build Hatası**
```
Hata: COMPILATION ERROR
Çözüm: Java JDK 21+ ve Maven 3.3+ kurulu olduğunu kontrol et
```

**3. Port Çakışması**
```
Hata: Port 8080 zaten kullanımda
Çözüm: application.properties'te server.port değiştir
```

**4. CORS Hatası**
```
Hata: Access-Control-Allow-Origin
Çözüm: Controller'da @CrossOrigin(origins = "*") olduğundan emin ol
```

---

## 📞 Destek

### Dokümantasyon

- [MySQL Kurulum Rehberi](MYSQL-WORKBENCH-KURULUM.md)
- [Yönetici Kurulum Rehberi](YONETICI-KURULUMU.md)
- [Uygulama Başlatma Rehberi](UYGULAMA-BASLATMA.md)

---

## 🎯 Gelecek Planlar

### Kısa Vadeli (1-2 Hafta)

- [ ] IT Dashboard API entegrasyonu
- [ ] Maintenance Dashboard API entegrasyonu
- [ ] Unit test'ler ekleme
- [ ] API dokümantasyonu (Swagger)

### Orta Vadeli (1-2 Ay)

- [ ] JWT token implementasyonu
- [ ] Redis caching ekleme
- [ ] CI/CD pipeline kurma
- [ ] Monitoring ve logging

### Uzun Vadeli (3-6 Ay)

- [ ] Microservices mimarisine geçiş
- [ ] Kubernetes deployment
- [ ] Advanced security features
- [ ] Mobile uygulama

---

## ✅ Onaylanan Özellikler

- [x] MySQL veritabanı entegrasyonu
- [x] REST API endpoint'leri
- [x] Rol bazlı yetkilendirme
- [x] 7 dashboard çalışıyor
- [x] Türkçe arayüz
- [x] LocalStorage → API geçişi
- [x] ID tipi düzeltmesi (Long → Integer)
- [x] H2 dependency kaldırıldı
- [x] Türkçe Javadoc açıklamaları
- [x] Temiz başlatma scriptleri

---

## 📜 Lisans

MIT License

---

**Proje Durumu:** ✅ Sunuma Hazır  
**Son Test Tarihi:** 30 Nisan 2026
