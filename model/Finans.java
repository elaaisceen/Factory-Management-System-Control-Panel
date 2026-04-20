package com.factory.stitch.model;


/**
 * Bu sinif Fabrika ERP backend modulu icin dokumante edilmis Java bileşenidir.
 */
public class Finans extends Department implements SurecYurutulebilir {

    public Finans(String sorumluPersonel) {
        super("Finans", sorumluPersonel);
    }

    public void butcePlanla() {
        islemAnimasyonu();
        System.out.println("Aylik fabrika butcesi ve odenekler planlandi.");
    }

    public void muhasebeIslemi() {
        islemAnimasyonu();
        System.out.println("Faturalar ve giderler muhasebelestirildi.");
    }

    @Override
    public void departmanSureciniYurut() {
        butcePlanla();
        muhasebeIslemi();
    }
}

