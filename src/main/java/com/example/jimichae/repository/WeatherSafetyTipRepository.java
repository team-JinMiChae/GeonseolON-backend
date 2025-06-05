package com.example.jimichae.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.jimichae.entity.WeatherSafetyTip;
import com.example.jimichae.entity.WeatherType;

@Repository
public interface WeatherSafetyTipRepository extends JpaRepository<WeatherSafetyTip,Long> {
	WeatherSafetyTip findByType(WeatherType weatherType);
}
