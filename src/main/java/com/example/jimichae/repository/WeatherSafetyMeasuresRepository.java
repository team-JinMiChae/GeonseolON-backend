package com.example.jimichae.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.jimichae.entity.WeatherSafetyMeasures;
import com.example.jimichae.entity.WeatherSafetyTip;

@Repository
public interface WeatherSafetyMeasuresRepository extends JpaRepository<WeatherSafetyMeasures,Long> {
	List<WeatherSafetyMeasures> findAllByWeatherSafetyTip(WeatherSafetyTip weatherSafetyTip);
}
