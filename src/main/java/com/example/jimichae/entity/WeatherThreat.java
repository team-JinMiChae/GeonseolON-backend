package com.example.jimichae.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
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
@SequenceGenerator(name = "weather_threat_seq", sequenceName = "weather_threat_seq", allocationSize = 1, initialValue = 1)
@AllArgsConstructor
@NoArgsConstructor
public class WeatherThreat {
	@Id
	@GeneratedValue(strategy = jakarta.persistence.GenerationType.SEQUENCE, generator = "weather_threat_seq")
	private Long id;

	@ManyToOne
	private WeatherSafetyTip weatherSafetyTip;

	@ManyToOne
	private Threat threat;
}
