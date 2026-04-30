package com.factory.stitch.repository;

import com.factory.stitch.model.Bordro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BordroRepository extends JpaRepository<Bordro, Integer> {
    // Ozel sorgular buraya eklenebilir.
}
