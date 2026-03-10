//şuanlık terminalde 

package com.fabrika;

import com.fabrika.model.*; 
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        // departmanları main içinde tanımladık
        HumanResources hr = new HumanResources("Ahmet Y.");
        Uretim uretim = new Uretim("Mehmet K.");
        Depo depo = new Depo("Ali Rıza");
        It it = new It("Ela Eda");
        SatinAlma satinAlma = new SatinAlma("Gizem S.");
        Finans finans = new Finans("Aylin T.");

        System.out.println("===== " + "FABRİKA YÖNETİM SİSTEMİ" + " =====");
        System.out.println("Lütfen Yetki Seçiniz:");
        System.out.println("1. İK | 2. Üretim | 3. Depo | 4. BT | 5. Satın Alma | 6. Finans");
        System.out.print("Seçim: ");

        int secim = input.nextInt();
        input.nextLine(); 

        // yetki matrisini bağladık
        switch (secim) {
            case 1:
                System.out.println("\nHoş geldin, " + hr.getSorumluPersonel());
                hr.durumGoster();
                hr.iseAlimYap("Yeni Personel");
                hr.bordroHazirla();
                break;
            case 2:
                System.out.println("\nHoş geldin, " + uretim.getSorumluPersonel());
                uretim.durumGoster();
                uretim.planlamaYap("PRJ-101");
                uretim.montajYap();
                break;
            case 3:
                depo.stokGirisCikis("Demir Profil", "GİRİŞ");
                depo.kritikStokKontrol();
                break;
            case 4:
                System.out.println("\nHoş geldin, " + it.getSorumluPersonel() + " (BT Yetkilisi)");
                it.durumGoster();
                it.sistemYonetimi();
                it.veriYedekle();
                break;

            case 5:
                System.out.println("\nHoş geldin, " + satinAlma.getSorumluPersonel() + " (Satın Alma Yetkilisi)");
                satinAlma.durumGoster();
                satinAlma.tedarikciEkle("Demirtaş A.Ş."); 
                satinAlma.malzemeAl("Çelik Vida", 5000);
                break;

            case 6:
                System.out.println("\nHoş geldin, " + finans.getSorumluPersonel() + " (Finans Yetkilisi)");
                finans.durumGoster();
                finans.butcePlanla(); 
                finans.muhasebeIslemi();
                break;

            // ekleme olursa aynı mantıkla
            default:
                System.out.println("Geçersiz yetki!");
        }
        input.close();
    }
}