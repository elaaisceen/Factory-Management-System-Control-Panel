package com.factory.stitch.service;


/**
 * Bu sinif Fabrika ERP backend modulu icin dokumante edilmis Java bileşenidir.
 */
import com.factory.stitch.model.Bordro;
import com.factory.stitch.repository.BordroRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class MaasHesaplamaService {

    private final BordroRepository bordroRepository;

    public MaasHesaplamaService(BordroRepository bordroRepository) {
        this.bordroRepository = bordroRepository;
    }

    @Transactional
    public Bordro bordroyuOnaylaVeKaydet(Bordro taslakBordro) {
        if (taslakBordro == null) {
            throw new IllegalArgumentException("Bordro payload bos olamaz.");
        }
        if (taslakBordro.getPersonelAd() == null || taslakBordro.getPersonelAd().isBlank()) {
            throw new IllegalArgumentException("Personel adi zorunludur.");
        }

        if (taslakBordro.getBazMaas() == null) {
            taslakBordro.setBazMaas(BigDecimal.ZERO);
        }
        if (taslakBordro.getMesaiUcreti() == null) {
            taslakBordro.setMesaiUcreti(BigDecimal.ZERO);
        }
        if (taslakBordro.getYanHaklarToplami() == null) {
            taslakBordro.setYanHaklarToplami(BigDecimal.ZERO);
        }
        if (taslakBordro.getKesintiler() == null) {
            taslakBordro.setKesintiler(BigDecimal.ZERO);
        }
        if (taslakBordro.getNetOdenen() == null) {
            BigDecimal net = taslakBordro.getBazMaas()
                    .add(taslakBordro.getMesaiUcreti())
                    .add(taslakBordro.getYanHaklarToplami())
                    .subtract(taslakBordro.getKesintiler());
            taslakBordro.setNetOdenen(net);
        }

        taslakBordro.setDurum("Yatirildi");
        taslakBordro.setOlusturmaTarihi(LocalDateTime.now());
        return bordroRepository.save(taslakBordro);
    }
}

