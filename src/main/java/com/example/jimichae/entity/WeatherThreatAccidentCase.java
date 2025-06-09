package com.example.jimichae.entity;

import static jakarta.persistence.GenerationType.*;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@SequenceGenerator(name = "weather_threat_accident_case_seq", sequenceName = "weather_threat_accident_case_seq", allocationSize = 1)
@AllArgsConstructor
@NoArgsConstructor
public class WeatherThreatAccidentCase {
	@Id
	@GeneratedValue(strategy =SEQUENCE, generator = "weather_threat_accident_case_seq")
	private Long id;

	@ManyToOne
	WeatherThreat weatherThreat;

	@ManyToOne
	WeatherAccidentCase weatherAccidentCase;
}
