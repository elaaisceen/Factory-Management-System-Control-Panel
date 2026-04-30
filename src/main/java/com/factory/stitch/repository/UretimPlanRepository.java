package com.factory.stitch.repository;

import com.factory.stitch.model.UretimPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UretimPlanRepository extends JpaRepository<UretimPlan, Integer> {
    List<UretimPlan> findByDepartmanId(Integer departmanId);
    List<UretimPlan> findByDurum(String durum);
}
