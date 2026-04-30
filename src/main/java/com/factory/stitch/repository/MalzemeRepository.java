package com.factory.stitch.repository;

import com.factory.stitch.model.Malzeme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MalzemeRepository extends JpaRepository<Malzeme, Integer> {
    List<Malzeme> findByStokMiktariLessThanEqual(Integer kritikLimit);
}
