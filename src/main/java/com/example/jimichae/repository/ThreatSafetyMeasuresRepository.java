package com.example.jimichae.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.jimichae.entity.ThreatSafetyMeasures;

@Repository
public interface ThreatSafetyMeasuresRepository extends JpaRepository<ThreatSafetyMeasures, Long> {
}
