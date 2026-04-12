package com.factory.stitch.service;

import com.factory.stitch.model.Bordro;
import com.factory.stitch.repository.BordroRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MaasHesaplamaService {

    private final BordroRepository bordroRepository;

    public MaasHesaplamaService(BordroRepository bordroRepository) {
        this.bordroRepository = bordroRepository;
    }

    /**
     * Frontend'den gelen son onaylanmış bordro yapısını alıp işler.
     * Transactional yapı burada devreye girer.
     */
    @Transactional
    public Bordro bordroyuOnaylaVeKaydet(Bordro taslakBordro) {
        
        System.out.println("Yeni Bordro İşleniyor... Personel: " + taslakBordro.getPersonelAd());
        System.out.println("Net Odenen Tutar: " + taslakBordro.getNetOdenen());

        // Varsayılan değerler (eğer frontend'den eksik gelirse)
        if (taslakBordro.getBazMaas() == null) taslakBordro.setBazMaas(BigDecimal.ZERO);
        
        // Durumu güncelle
        taslakBordro.setDurum("Yatırıldı");
        taslakBordro.setOlusturmaTarihi(LocalDateTime.now());
        
        // JPA repository ile kayıt yapılır
        return bordroRepository.save(taslakBordro);
    }
}

