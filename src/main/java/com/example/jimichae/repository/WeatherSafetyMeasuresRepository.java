package com.example.jimichae.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.jimichae.entity.WeatherSafetyMeasures;

@Repository
public interface WeatherSafetyMeasuresRepository extends JpaRepository<WeatherSafetyMeasures,Long> {
}
