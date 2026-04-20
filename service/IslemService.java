package com.factory.stitch.service;

import com.factory.stitch.model.Islem;
import com.factory.stitch.repository.IslemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * IslemService, is adimlarini bellek ici repository uzerinden yonetir.
 * Bu katman, controller ile repository arasinda is kurali noktasi olarak kullanilir.
 */
@Service
public class IslemService {

    private final IslemRepository islemRepository = new IslemRepository();

    /**
     * Yeni bir islem kaydi olusturur.
     */
    public Islem kaydet(String aciklama) {
        Islem islem = new Islem(aciklama);
        return islemRepository.save(islem);
    }

    /**
     * Tum islem kayitlarini getirir.
     */
    public List<Islem> tumunuGetir() {
        return islemRepository.findAll();
    }

    /**
     * Id ile tek kaydi getirir.
     */
    public Optional<Islem> idIleGetir(int id) {
        return islemRepository.findById(id);
    }

    /**
     * Id'ye gore kayit siler.
     */
    public boolean sil(int id) {
        return islemRepository.deleteById(id);
    }
}
