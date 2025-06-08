package com.example.jimichae.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.jimichae.entity.WeatherAccidentCase;

@Repository
public interface WeatherAccidentCaseRepository extends JpaRepository<WeatherAccidentCase, Long> {
	boolean existsByBoardNo(int boardNo);

	WeatherAccidentCase findByBoardNo(int boardNo);
}
