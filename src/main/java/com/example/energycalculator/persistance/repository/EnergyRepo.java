package com.example.energycalculator.persistance.repository;

import com.example.energycalculator.persistance.entity.EnergyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnergyRepo extends JpaRepository<EnergyEntity, Long> {

  EnergyEntity findFirstByOrderByDateCreatedDesc();
}
