# Fabrika ERP Simulasyonu - Odev Durum Raporu

## Proje Amaci
Fabrikanin IK, Uretim, Depo, BT, Satin Alma ve Finans departmanlarini OOP prensipleriyle tek bir terminal ERP simulasyonunda toplamak.

## Mevcut Durum (08.03.2026 itibari ile biten islemler)
- Calisan terminal uygulamasi var (`com.fabrika.Main`).
- Role-based menu ve departman bazli yetkili ekran mevcut.
- 6 departman sinifi `Department` taban sinifindan kalitim aliyor.
- Her departmanin surec metotlari calisiyor (ise alim, planlama, stok, butce vb.).
- Interface entegrasyonu eklendi (`SurecYurutulebilir`).
- Dosya islemleri eklendi (`DosyaKayitUtil`) ve `logs/islem-kaydi.txt` olusuyor.
### 1. Hafta Kullanılan Mimariler ve Nedenleri
#### DOSYA YÖNETİMİ VE LOG SİSTEMİ için kullanıdığımız mimari ve nedenleri
- Java NIO.2 (New Input/Output)
Projemizin dosya kayıt sistemini geliştirirken geleneksel `java.io` kütüphanesi yerine modern `java.nio` kütüphanesini tercih ettik. Bunun temel nedenleri şunlardır;
1- Platform Bağımsızlık: Path arayüzü sayesinde kodumuzun hem Windows hem de Linux tabanlı sistemlerde dosya yolu hatası almadan çalışmasını sağladık.
2- Verimlilik: `Files.writeString` metodu ile bellek yönetimini optimize ederek, dosya açma/kapama işlemlerini daha güvenli ve kısa kod blokları ile gerçekleştirdik.
3- Hata Yönetimi: Klasörlerin varlığını kontrol eden otomatik mekanizmalar kullanarak `(createDirectories)`, çalışma anında (runtime) oluşabilecek "Dosya Bulunamadı" hatalarının önüne geçtik.
4- Standartlaştırma: Tüm karakter kodlamalarını UTF-8 olarak sabitleyerek veri bütünlüğünü koruduk.
Sonuç olarak, bu yapı sayesinde projemiz daha sürdürülebilir, okunaklı ve profesyonel standartlara uygun bir logging (günlükleme) mekanizmasına sahip olmuştur.
#### Main.java dosyasında kullandığımız yapılar ve nedenleri
- Map (Sözlük) Yapısı
    * if-else kalabalığından kurtulmak için. Sayı girildiğinde direkt ilgili Departman nesnesini getirir.
    * LinkedHashMap seçtik çünkü ekleme sırasını korur, menü düzgün görünür.
- Loose Coupling (Gevşek Bağlılık): Main sınıfı, It veya Uretim sınıflarının iç detaylarını bilmez. Sadece Department olduklarını bilir. Bu sayede yarın yeni bir departman (örn: Pazarlama) eklediğinde Main kodunu neredeyse hiç değiştirmeden sisteme dahil edebilirsin.
- Polymorphism (Çok Biçimlilik): `rolMatrisi.get(secim)` satırı aslında Java'nın en güçlü yanıdır. Tek bir Department referansı, çalışma anında (runtime) hangi sınıfa aitse onun metodunu çağırır.
- Interface Kullanımı (SurecYurutulebilir): Her departmanın işleyişi farklıdır. Bazıları sadece rapor sunarken (Durum Goster), bazıları aktif iş yapar (Süreç Yürüt). Interface kullanarak bu "yeteneği" sadece ihtiyacı olan sınıflara kazandırdık.
- Hata Yönetimi ve Temiz Kod: `sayiOku` metodu gibi yardımcı metodlar sayesinde, kullanıcı hatalı giriş yaptığında programın patlamasını engelledik (Robust Programming).
#### SatinAlma.java dosyasında kullandığımız yapılar ve nedenleri
- Kodun Yeniden Kullanılabilirliği (Inheritance): SatinAlma sınıfı Department sınıfından miras aldığı için `getDepartmanAdi()` veya `islemAnimasyonu()` gibi metotları tekrar yazmamıza gerek kalmadı. Bu, projenin şişmesini engeller.
- Arayüz Kontrolü (Interface): `SurecYurutulebilir` interface'ini kullanarak bu departmana bir "iş yapabilme yeteneği" kazandırdık. Main sınıfında hatırlarsan instanceof ile bu yeteneği kontrol ediyorduk. Eğer bir departman sadece rapor veriyorsa bu interface'i eklemeyiz, böylece hata yapma payımız azalır.
- Genişletilebilirlik: Projeye `"İhracat"` departmanı eklemek istersek, sadece bu şablonu kopyalayıp metodun içini değiştirmen yeterli olacaktır. Sistemin geri kalanı buna uyum sağlayacaktır.
#### Uretim.java dosyasında kullandığımız yapılar ve nedenleri
- Sorumlulukların Ayrılması (Single Responsibility): Her metodun tek bir görevi var (sadece planlama, sadece montaj gibi). Bu sayede ileride "Sadece kalite kontrol yap" demek istersek, tüm süreci baştan çalıştırmadan ilgili metodu çağırabiliriz.
- İş Akışı Yönetimi (Workflow Orchestration): `departmanSureciniYurut` metodu aslında bir "orkestra şefi" gibi davranır. Metotların hangi sırayla çalışacağını o belirler.
- Görsel Geri Bildirim: islemAnimasyonu() metodunu her aşamaya ekleyerek, konsol tabanlı bir uygulamada kullanıcıya "arka planda bir şeyler dönüyor" hissini veriyoruz. 
#### SurecYurutulebilir.java
- Esneklik (Flexibility): Fabrikadaki her departman bir `Department`'tır ama her departman bir "süreç" yürütmez. Mesela Finans sadece rapor sunarken, Uretim bir şeyler üretir. Bu arayüz sayesinde, sadece süreç yürütme yeteneği olan departmanlara bu özelliği kazandırdık.
- Sözleşme Mantığı (Contract): Bir sınıfa implements `SurecYurutulebilir` yazdıldığı an, Java "Bu sınıfın içine `departmanSureciniYurut` metodunu yazmazsan kodu çalıştırmam!" der. Kodun standart kalmasını sağlar. 
- Polymorphism (Çok Biçimlilik): Main sınıfında ((SurecYurutulebilir) departman) şeklinde bir dönüşüm yapmıştık. Bu arayüz sayesinde, nesnenin `Uretim` mi yoksa `SatinAlma` mı olduğuna bakmaksızın, "Sen süreç yürütebiliyor musun? O zaman yürüt." diyebiliyoruz.
- Gevşek Bağlılık (Loose Coupling): Sistem, sınıfların isimlerine değil, yeteneklerine (Interface'lerine) bakar. Bu da yeni bir süreç tipi eklediğinde sistemin çökmemesini sağlar.

## Hafta Hafta Ilerleme Ozeti
### 1. Hafta
1. Problem analizi, ERP kapsam tanimi, paketleme ve N-tier taslak yapisi.
2. `Department` ust sinifi ve 6 alt sinif ile kalitim yapisi.
3. Departmanlara ozel islevlerin yazilmasi ve simulasyon metotlari.
4. Scanner tabanli console menu ve departman gecisleri.
5. Role-based secim (yetki matrisi mantigi) ve akis duzenleme.
6. Interface konusu entegrasyonu (`SurecYurutulebilir`) ve surec standardizasyonu.
7. Dosya islemleri (loglama), islem ciktisinin dosyaya kaydi.

## Sonraki Asama (Simdilik Bekleyen)
- JavaFX UI katmani (ayri teslim fazi olarak planlandi).
- Repository/service katmanlarinin (su an bos olan siniflar) veriye baglanarak doldurulmasi.
- Kalicilik tarafinda JSON/CSV veya DB baglantisi secimi ile daha kurumsal veri saklama.
