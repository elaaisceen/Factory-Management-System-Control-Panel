package com.factory.stitch.model;

public class Malzeme extends BaseEntity {
    private String malzemeAdi;

    public Malzeme() {
    }

    public Malzeme(String malzemeAdi) {
        this.malzemeAdi = malzemeAdi;
    }

    public String getMalzemeAdi() {
        return malzemeAdi;
    }

    public void setMalzemeAdi(String malzemeAdi) {
        this.malzemeAdi = malzemeAdi;
    }
}
