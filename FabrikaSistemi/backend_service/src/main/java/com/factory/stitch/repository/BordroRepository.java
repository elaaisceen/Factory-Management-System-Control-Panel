package com.factory.stitch.repository;

import com.factory.stitch.model.Bordro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import javax.persistence.*;
import java.math.BigDecimal;

@Repository
public interface BordroRepository extends JpaRepository<Bordro, Integer> {
    // Özel sorgular buraya eklenebilir
}
