package com.example.jimichae.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.jimichae.entity.WeatherAccidentCase;
import com.example.jimichae.entity.WeatherThreat;
import com.example.jimichae.entity.WeatherThreatAccidentCase;

@Repository
public interface WeatherThreatAccidentCaseRepository extends JpaRepository<WeatherThreatAccidentCase, Long> {
	boolean existsByWeatherThreatAndWeatherAccidentCase(WeatherThreat weatherThreat, WeatherAccidentCase weatherAccidentCase);
	List<WeatherThreatAccidentCase> findAllByWeatherThreat(WeatherThreat weatherThreat);
}
