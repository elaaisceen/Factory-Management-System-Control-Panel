package com.factory.stitch.model;

/**
 * Bu bir Interface'dir (Arayüz). 
 * Nesne oluşturamaz, sadece alt sınıfların ne yapması gerektiğini "taahhüt" altına alır.
 */

public interface SurecYurutulebilir {

    /**
     * Bu metot gövdesizdir (abstract). 
     * Bu arayüzü 'implements' eden her sınıf, bu metodu kendi içine @Override ederek doldurulmak zorundadır.
     */

    void departmanSureciniYurut();
}
