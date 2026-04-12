package com.fabrika.model;

public class Islem extends BaseEntity {
    private String aciklama;

    public Islem() {
    }

    public Islem(String aciklama) {
        this.aciklama = aciklama;
    }

    public String getAciklama() {
        return aciklama;
    }

    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }
}
