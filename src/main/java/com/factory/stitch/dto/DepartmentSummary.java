package com.factory.stitch.dto;


/**
 * Bu sinif Fabrika ERP backend modulu icin dokumante edilmis Java bileşenidir.
 */
import java.math.BigDecimal;

public class DepartmentSummary {
    private String code;
    private String departmanAdi;
    private String sorumluPersonel;
    private int calisanSayisi;
    private BigDecimal aylikButce;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDepartmanAdi() {
        return departmanAdi;
    }

    public void setDepartmanAdi(String departmanAdi) {
        this.departmanAdi = departmanAdi;
    }

    public String getSorumluPersonel() {
        return sorumluPersonel;
    }

    public void setSorumluPersonel(String sorumluPersonel) {
        this.sorumluPersonel = sorumluPersonel;
    }

    public int getCalisanSayisi() {
        return calisanSayisi;
    }

    public void setCalisanSayisi(int calisanSayisi) {
        this.calisanSayisi = calisanSayisi;
    }

    public BigDecimal getAylikButce() {
        return aylikButce;
    }

    public void setAylikButce(BigDecimal aylikButce) {
        this.aylikButce = aylikButce;
    }
}

