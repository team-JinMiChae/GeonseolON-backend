package com.example.jimichae.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.jimichae.dto.response.WeatherInfoResponse;
import com.example.jimichae.service.WeatherGuideService;

@RestController
@RequestMapping("/api/v1/weather_guide")
public class WeatherGuideController {
	private final WeatherGuideService weatherGuideService;

	public WeatherGuideController(WeatherGuideService weatherGuideService) {
		this.weatherGuideService = weatherGuideService;
	}

	@GetMapping
	public WeatherInfoResponse getWeather(
		@RequestParam("latitude")
		double latitude,
		@RequestParam("longitude")
		double longitude,
		@RequestParam(value = "regionName")
		String regionName
	){
		return weatherGuideService.getWeatherGuide(latitude, longitude, regionName);
	}
}
