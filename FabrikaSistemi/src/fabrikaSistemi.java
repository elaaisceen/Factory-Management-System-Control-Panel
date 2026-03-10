import java.util.Scanner;

public class fabrikaSistemi {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        // Sisteme departmanları tanımlıyoruz (Senin verdiğin sınıf isimleriyle)
        HumanResources hr = new HumanResources("Ahmet Y.");
        Uretim uretim = new Uretim("Mehmet K.");
        Depo depo = new Depo("Ali Rıza");
        It it = new It("Ela Eda");
        SatinAlma satinAlma = new SatinAlma("Gizem S.");
        Finans finans =new Finans("Aylin T.");

        System.out.println("===== FABRİKA ERP SİSTEMİNE GİRİŞ =====");
        System.out.println("Lütfen Rolünüzü (Yetkinizi) Seçin:");
        System.out.println("1. İnsan Kaynakları Yöneticisi");
        System.out.println("2. Üretim Müdürü");
        System.out.println("3. Depo Sorumlusu");
        System.out.println("4. Bilgi İşlem Yöneticisi");
        System.out.println("5. Satın Alma Müdürü");
        System.out.println("6. Finans Müdürü");
        System.out.print("Rol Seçimi: ");

        int rolSecim = input.nextInt();
        input.nextLine();

        // YETKİ MATRİSİ: Kullanıcı sadece yetkisi olan menüyü görür
        if (rolSecim == 1) {
            System.out.println("\nHoş geldin, " + hr.sorumluPersonel + " (İK Yetkilisi)");
            hr.durumGoster();
            hr.iseAlimYap("Veli Demir");


        } else if (rolSecim == 2) {
            System.out.println("\nHoş geldin, " + uretim.sorumluPersonel + " (Üretim Yetkilisi)");
            uretim.durumGoster();
            uretim.planlamaYap("");
            uretim.montajYap();
            uretim.kaliteKontrol();

        } else if (rolSecim == 3) {
            System.out.println("\nHoş geldin, " + depo.sorumluPersonel + " (Depo Yetkilisi)");
            depo.durumGoster();
            depo.stokGirisCikis("Demir Profil", "GİRİŞ");
            depo.kritikStokKontrol();

        }  else if (rolSecim == 4) {
            System.out.println("\nHoş geldin, " +it.sorumluPersonel + " (Bilgi İşlem Yetkilisi)");
            it.durumGoster();
            it.sistemYonetimi();
            it.veriYedekle();

         } else if (rolSecim == 5) {
            System.out.println("\nHoş geldin, " + satinAlma.sorumluPersonel + " (Satın Alma Yetkilisi)");
            satinAlma.durumGoster();
            satinAlma.tedarikciEkle("Demirtaş A.Ş."); // Örnek bir tedarikçi ekliyoruz
            satinAlma.malzemeAl("Çelik Vida", 5000); // Örnek bir malzeme alımı yapıyoruz

         } else if (rolSecim == 6) {
            System.out.println("\nHoş geldin, " + finans.sorumluPersonel + " (Finans Yetkilisi)");
            finans.durumGoster();
            finans.butcePlanla();
            finans.muhasebeIslemi();
        } else {
            System.out.println("Yetkisiz giriş veya geçersiz rol!");
         }

        input.close();
    }
}