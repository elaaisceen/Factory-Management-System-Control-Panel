package com.factory.stitch.dto;

import java.math.BigDecimal;

public class DashboardSummary {
    private int toplamPersonel;
    private int kritikStokAdedi;
    private int acikSiparisAdedi;
    private int aktifUretimProjesi;
    private BigDecimal toplamAylikButce;

    public int getToplamPersonel() {
        return toplamPersonel;
    }

    public void setToplamPersonel(int toplamPersonel) {
        this.toplamPersonel = toplamPersonel;
    }

    public int getKritikStokAdedi() {
        return kritikStokAdedi;
    }

    public void setKritikStokAdedi(int kritikStokAdedi) {
        this.kritikStokAdedi = kritikStokAdedi;
    }

    public int getAcikSiparisAdedi() {
        return acikSiparisAdedi;
    }

    public void setAcikSiparisAdedi(int acikSiparisAdedi) {
        this.acikSiparisAdedi = acikSiparisAdedi;
    }

    public int getAktifUretimProjesi() {
        return aktifUretimProjesi;
    }

    public void setAktifUretimProjesi(int aktifUretimProjesi) {
        this.aktifUretimProjesi = aktifUretimProjesi;
    }

    public BigDecimal getToplamAylikButce() {
        return toplamAylikButce;
    }

    public void setToplamAylikButce(BigDecimal toplamAylikButce) {
        this.toplamAylikButce = toplamAylikButce;
    }
}
