package com.factory.stitch.repository;

import com.factory.stitch.model.MuhasebeKayit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MuhasebeKayitRepository extends JpaRepository<MuhasebeKayit, Integer> {
    List<MuhasebeKayit> findByDepartmanId(Integer departmanId);
    List<MuhasebeKayit> findByKayitTuru(String kayitTuru);
}
