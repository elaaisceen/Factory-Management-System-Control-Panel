package com.factory.stitch.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.persistence.*; //(Java 11 ve Spring Boot 2 için)

@Entity
@Table(name = "bordro")
public class Bordro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer bordroId;

    @Column(name = "personel_ad", nullable = false)
    private String personelAd;

    @Column(name = "donem_ay_yil", length = 7)
    private String donemAyYil;

    @Column(name = "baz_maas")
    private BigDecimal bazMaas;

    @Column(name = "calisilan_gun")
    private Integer calisilanGun;

    @Column(name = "mesai_saati")
    private Integer mesaiSaati;

    @Column(name = "mesai_ucreti")
    private BigDecimal mesaiUcreti;

    @Column(name = "yan_haklar_toplami")
    private BigDecimal yanHaklarToplami;

    @Column(name = "kesintiler")
    private BigDecimal kesintiler;

    @Column(name = "net_odenen")
    private BigDecimal netOdenen;

    @Column(name = "durum")
    private String durum;

    @Column(name = "olusturma_tarihi")
    private LocalDateTime olusturmaTarihi;

    // Getters and Setters
    public Integer getBordroId() {
        return bordroId;
    }

    public void setBordroId(Integer bordroId) {
        this.bordroId = bordroId;
    }

    public String getPersonelAd() {
        return personelAd;
    }

    public void setPersonelAd(String personelAd) {
        this.personelAd = personelAd;
    }

    public String getDonemAyYil() {
        return donemAyYil;
    }

    public void setDonemAyYil(String donemAyYil) {
        this.donemAyYil = donemAyYil;
    }

    public BigDecimal getBazMaas() {
        return bazMaas;
    }

    public void setBazMaas(BigDecimal bazMaas) {
        this.bazMaas = bazMaas;
    }

    public Integer getCalisilanGun() {
        return calisilanGun;
    }

    public void setCalisilanGun(Integer calisilanGun) {
        this.calisilanGun = calisilanGun;
    }

    public Integer getMesaiSaati() {
        return mesaiSaati;
    }

    public void setMesaiSaati(Integer mesaiSaati) {
        this.mesaiSaati = mesaiSaati;
    }

    public BigDecimal getMesaiUcreti() {
        return mesaiUcreti;
    }

    public void setMesaiUcreti(BigDecimal mesaiUcreti) {
        this.mesaiUcreti = mesaiUcreti;
    }

    public BigDecimal getYanHaklarToplami() {
        return yanHaklarToplami;
    }

    public void setYanHaklarToplami(BigDecimal yanHaklarToplami) {
        this.yanHaklarToplami = yanHaklarToplami;
    }

    public BigDecimal getKesintiler() {
        return kesintiler;
    }

    public void setKesintiler(BigDecimal kesintiler) {
        this.kesintiler = kesintiler;
    }

    public BigDecimal getNetOdenen() {
        return netOdenen;
    }

    public void setNetOdenen(BigDecimal netOdenen) {
        this.netOdenen = netOdenen;
    }

    public String getDurum() {
        return durum;
    }

    public void setDurum(String durum) {
        this.durum = durum;
    }

    public LocalDateTime getOlusturmaTarihi() {
        return olusturmaTarihi;
    }

    public void setOlusturmaTarihi(LocalDateTime olusturmaTarihi) {
        this.olusturmaTarihi = olusturmaTarihi;
    }
}

