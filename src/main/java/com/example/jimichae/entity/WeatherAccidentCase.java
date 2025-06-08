package com.example.jimichae.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@SequenceGenerator(name = "weather_accident_case_seq", sequenceName = "weather_accident_case_seq", allocationSize = 1)
@AllArgsConstructor
@NoArgsConstructor
public class WeatherAccidentCase {
	@Id
	@GeneratedValue(strategy = jakarta.persistence.GenerationType.SEQUENCE, generator = "weather_accident_case_seq")
	private Long id;

	@Column(nullable = false)
	private int boardNo;

	@Column(nullable = false, length = 100)
	private String title;

	@Column(nullable = false, length = 200)
	private String summation;

	@Column(nullable = false, length = 1000)
	private String fileUrl;
}
