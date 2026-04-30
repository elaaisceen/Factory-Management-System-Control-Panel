package com.factory.stitch.service;

import com.factory.stitch.model.Malzeme;
import com.factory.stitch.repository.MalzemeRepository;
import com.factory.stitch.util.KritikKontrolUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * MalzemeService, depo/malzeme verisini OOP mantiginda servis katmaninda yonetir.
 */
@Service
public class MalzemeService {

    @Autowired
    private MalzemeRepository malzemeRepository;

    /**
     * Yeni malzeme olusturup kaydeder.
     */
    public Malzeme kaydet(String malzemeAdi) {
        Malzeme malzeme = new Malzeme(malzemeAdi);
        return malzemeRepository.save(malzeme);
    }

    /**
     * Tum malzemeleri getirir.
     */
    public List<Malzeme> tumunuGetir() {
        return malzemeRepository.findAll();
    }

    /**
     * Id ile malzeme arar.
     */
    public Optional<Malzeme> idIleGetir(Integer id) {
        return malzemeRepository.findById(id);
    }

    /**
     * Id ile malzeme siler.
     */
    public boolean sil(Integer id) {
        if (malzemeRepository.existsById(id)) {
            malzemeRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Girilen stok miktarina gore kritik seviyede olup olmadigini doner.
     */
    public boolean kritikMi(int stokMiktari, int kritikEsik) {
        return KritikKontrolUtil.kritikSeviyeMi(stokMiktari, kritikEsik);
    }
}
