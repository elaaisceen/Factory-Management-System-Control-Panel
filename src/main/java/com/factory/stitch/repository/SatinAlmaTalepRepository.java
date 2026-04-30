package com.factory.stitch.repository;

import com.factory.stitch.model.SatinAlmaTalep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SatinAlmaTalepRepository extends JpaRepository<SatinAlmaTalep, Integer> {
    List<SatinAlmaTalep> findByDepartmanId(Integer departmanId);
    List<SatinAlmaTalep> findByMalzemeId(Integer malzemeId);
    List<SatinAlmaTalep> findByDurum(String durum);
}
