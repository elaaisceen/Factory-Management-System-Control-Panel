# 🏭 Fabrika Yönetim Sistemi (Factory Management System)

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Spring](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-00000F?style=for-the-badge&logo=mysql&logoColor=white)
![HTML5](https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge&logo=html5&logoColor=white)

## 👩🏻‍💻 Biz Kimiz?
Bartın Üniversitesi, Bilgisayar Teknolojileri ve Bilişim Sistemleri (BTBS) bölümü öğrencileri olarak, **Nesneye Dayalı Programlama** dersi kapsamında bu projeyi gerçekleştirmekteyiz.

**Proje Ekibi:**
* **Elanur Tuana İŞCEN** - [GitHub Profili](https://github.com/elaaisceen)
* **Eda MISIRLIOĞLU** - [GitHub](https://github.com/edamisirlioglu)

---

## 📊 Modüller ve Ekranlar
Sistemimiz 6 ana departmanın entegre çalışmasını sağlar:

* 🔐 **Giriş & Yetkilendirme:** Rol bazlı (RBAC) erişim sistemi.
* 👥 **İnsan Kaynakları:** Personel yönetimi ve organizasyon şeması.
* 📦 **Stok & Depo:** Kritik limit uyarı sistemli envanter takibi.
* ⚙️ **Üretim:** Montaj ve kalite kontrol süreçlerinin izlenmesi.
* 💰 **Finans & Satın Alma:** Bütçe yönetimi ve tedarikçi ilişkileri.
* 🛠️ **BT / Sistem:** Veri güvenliği ve ağ yönetimi.

---

## 🛠️ Teknik Gereksinimler ve Kütüphaneler
Projenin backend ve veri katmanı aşağıdaki temel kütüphaneler üzerine inşa edilmiştir:

* **Java SE & JDK 11+:** Çekirdek dil ve standart kütüphaneler.
* **Spring Boot (Web & Data JPA):** RESTful API uçları ve veritabanı ORM yönetimi için.
* **MySQL Connector J:** İlişkisel veritabanı bağlantısı.
* **Java NIO.2:** Gelişmiş dosya giriş-çıkış (File I/O) ve UTF-8 formatında loglama.
* **Java Collections API:** `LinkedHashMap`, `ArrayList` ve `AtomicInteger` ile dinamik veri yönetimi.

---

## 📅 14 Haftalık Detaylı İş Planı
| Hafta | Aşama & Görevler | Durum |
| :--- | :--- | :--- |
| **1-2. Hafta** | **Analiz & Planlama:** Gereksinimlerin belirlenmesi ve iş akışlarının çizilmesi. | ✅ |
| **3. Hafta** | **Kavramsal Tasarım:** Departman yapısı ve organizasyon şemasının oluşturulması. | ✅ |
| **4-5. Hafta** | **UML & OOP:** Class & Sequence diyagramları, Inheritance yapısının kurulması. | ✅ |
| **6-7. Hafta** | **Veritabanı Tasarımı:** ER Diyagramı çizimi ve SQL Scriptlerinin yazılması. | ✅ |
| **8-10. Hafta** | **Backend Kodlama:** DAO & Model katmanları, CRUD servislerinin yazılması. | ⏳ |
| **11-12. Hafta** | **Yetkilendirme & Akış:** JWT/Login sistemi ve rol bazlı kontrollerin entegrasyonu. | 📅 |
| **13. Hafta** | **Test & Hata Ayıklama:** Birim testleri ve Exception Handling süreçleri. | 📅 |
| **14. Hafta** | **Dokümantasyon & Final:** Proje raporunun tamamlanması ve sunum hazırlığı. | 📅 |

---

## 🏗️ Yazılım Mimarisi
Proje, modülerliği ve sürdürülebilirliği sağlamak adına **Katmanlı Mimari (N-Tier)** ile geliştirilmektedir:
* **Controller:** `@RestController` ile API uçlarının yönetimi.
* **Service:** İş mantığı ve hesaplamaların (Örn: `MaasHesaplamaService`) yapıldığı katman.
* **Model (Entity):** Veritabanı tablolarının Java sınıfları ile eşleşmesi (`Bordro`, `Islem`, `Department`).
* **Repository:** `JpaRepository` ile veritabanı erişim standartlarının belirlenmesi.
* **Util:** `DosyaKayitUtil` gibi yardımcı araçların yönetimi.

---

## 📁 Paket Yapısı (Package Structure)
```text
src/main/java/com/factory
├── controller/  # UI element kontrolcüleri
├── service/     # İş mantığı ve hesaplamalar
├── model/       # Entity sınıfları (Üretim, Makine, Personel)
├── dao/         # Veritabanı (SQL) işlemleri
└── view/        # .fxml, .html ve .css kaynakları

---

## ⚙️ Mevcut Durum Notu
> **Not:** Sistem şu an Dashboard tasarımları ve Java altyapısı üzerinde geliştirilme aşamasındadır. Veritabanı bağlantısı (`DatabaseConnection`) aktif hale getirildiğinde, demo veriler yerini dinamik MySQL verilerine bırakacaktır.

---
*Bu proje Bartın Üniversitesi Bilgisayar Teknolojileri ve Bilişim Sistemleri Bölümü Nesneye Dayalı Programlama-II dersi için geliştirilmektedir.*
