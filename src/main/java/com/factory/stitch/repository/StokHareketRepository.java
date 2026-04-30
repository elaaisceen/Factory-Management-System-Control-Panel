package com.factory.stitch.repository;

import com.factory.stitch.model.StokHareket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StokHareketRepository extends JpaRepository<StokHareket, Integer> {
    List<StokHareket> findByMalzemeId(Integer malzemeId);
    List<StokHareket> findByHareketTuru(String hareketTuru);
}
