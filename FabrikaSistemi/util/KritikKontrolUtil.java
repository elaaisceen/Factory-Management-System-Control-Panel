package com.factory.stitch.util;

/**
 * KritikKontrolUtil, stok/kapasite gibi degerlerde kritik esik kontrolu yapar.
 */
public final class KritikKontrolUtil {

    private KritikKontrolUtil() {
        // Utility sinifindan nesne uretilmesini engeller.
    }

    /**
     * Miktar, esik degerin altinda veya esitse true doner.
     */
    public static boolean kritikSeviyeMi(int miktar, int esikDeger) {
        return miktar <= esikDeger;
    }

    /**
     * Kritik durumlar icin kullanilabilir standart mesaj uretir.
     */
    public static String kritikMesaj(String kaynakAdi, int miktar, int esikDeger) {
        if (!kritikSeviyeMi(miktar, esikDeger)) {
            return kaynakAdi + " normal seviyede.";
        }
        return "UYARI: " + kaynakAdi + " kritik seviyede. Mevcut=" + miktar + ", Esik=" + esikDeger;
    }
}
