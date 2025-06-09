package com.example.jimichae.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SequenceGenerator(name = "weather_safety_tips_seq", sequenceName = "weather_safety_tips_seq", allocationSize = 1)
public class WeatherSafetyTip {
	@Id
	@GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "weather_safety_tips_seq")
	private Long id;

	@Enumerated(EnumType.STRING)
	private WeatherType type;

	@Column(nullable = false, length = 1000)
	private String simpleSafetyMeasures;

	@Column(nullable = false, columnDefinition = "CLOB")
	private String detailedSafetyMeasures;
}
