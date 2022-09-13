package com.example.energycalculator.persistance.repository;

import com.example.energycalculator.persistance.entity.TarifEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TarifRepo extends JpaRepository<TarifEntity, Long> {

  TarifEntity findFirstByOrderByDateCreatedDesc();

}
