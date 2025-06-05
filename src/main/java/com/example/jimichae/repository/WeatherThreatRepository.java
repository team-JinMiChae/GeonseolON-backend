package com.example.jimichae.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.jimichae.entity.WeatherSafetyTip;
import com.example.jimichae.entity.WeatherThreat;

@Repository
public interface WeatherThreatRepository extends JpaRepository<WeatherThreat,Long> {
	List<WeatherThreat> findAllByWeatherSafetyTip(WeatherSafetyTip weatherSafetyTip);
}
