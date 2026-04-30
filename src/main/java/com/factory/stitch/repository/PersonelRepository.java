package com.factory.stitch.repository;

import com.factory.stitch.model.Personel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonelRepository extends JpaRepository<Personel, Integer> {
    List<Personel> findByDepartmanId(Integer departmanId);
    List<Personel> findByAktif(Boolean aktif);
}
