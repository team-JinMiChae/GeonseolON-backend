package com.example.jimichae.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.jimichae.entity.WeatherSafetyTip;
import com.example.jimichae.entity.WeatherSafetyTipSource;

@Repository
public interface WeatherSafetyTipSourceRepository extends JpaRepository<WeatherSafetyTipSource,Long> {
	List<WeatherSafetyTipSource> findAllByWeatherSafetyTip(WeatherSafetyTip weatherSafetyTip);
}
