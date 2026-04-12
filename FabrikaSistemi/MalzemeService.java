package com.fabrika.model;

public class HumanResources extends Department implements SurecYurutulebilir {

    public HumanResources(String sorumluPersonel) {
        super("Insan Kaynaklari", sorumluPersonel);
    }

    public void iseAlimYap(String personelAdi) {
        islemAnimasyonu();
        calisanSayisi += 1;
        System.out.println("Tebrikler! " + personelAdi + " fabrikamizda ise basladi.");
    }

    public void bordroHazirla() {
        islemAnimasyonu();
        System.out.println("Maaslar hesaplandi. Toplam " + calisanSayisi + " personele odeme yapildi.");
    }

    @Override
    public void departmanSureciniYurut() {
        iseAlimYap("Yeni Personel");
        bordroHazirla();
    }
}
