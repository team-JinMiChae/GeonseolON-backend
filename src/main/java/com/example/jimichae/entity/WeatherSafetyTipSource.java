package com.example.jimichae.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@SequenceGenerator(name = "weather_safety_tip_source_seq", sequenceName = "weather_safety_tip_source_seq", allocationSize = 1, initialValue = 1)
@AllArgsConstructor
@NoArgsConstructor
public class WeatherSafetyTipSource {
	@Id
	@GeneratedValue(strategy= GenerationType.SEQUENCE , generator = "weather_safety_tip_source_seq")
	private Long id;

	@ManyToOne
	private WeatherSafetyTip weatherSafetyTip;

	@ManyToOne
	private Source source;
}
