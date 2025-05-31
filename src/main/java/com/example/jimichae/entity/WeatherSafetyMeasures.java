package com.example.jimichae.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@SequenceGenerator(name = "weather_safety_measures_seq", sequenceName = "weather_safety_measures_seq", allocationSize = 1, initialValue = 1)
@AllArgsConstructor
@NoArgsConstructor
public class WeatherSafetyMeasures {
	@Id
	@GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "weather_safety_measures_seq")
	private Long id;

	@ManyToOne
	private WeatherSafetyTip weatherSafetyTip;

	@Column(nullable = false, columnDefinition = "CLOB")
	@Lob
	private String safetyMeasures;
}
