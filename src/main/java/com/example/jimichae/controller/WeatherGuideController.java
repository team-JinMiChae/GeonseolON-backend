package com.example.jimichae.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.jimichae.dto.request.WeatherGuide.WeatherDetailRequest;
import com.example.jimichae.dto.response.WeatherInfoResponse;
import com.example.jimichae.dto.response.WeatherTipResponse;
import com.example.jimichae.entity.WeatherType;
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
	) {
		return weatherGuideService.getWeatherGuide(latitude, longitude, regionName);
	}

	@PostMapping("/save")
	public void saveWeatherGuide(
		@RequestBody
		WeatherDetailRequest weatherDetailRequest
	) {
		if (weatherDetailRequest == null) {
			throw new IllegalArgumentException("Weather detail request cannot be null");
		}
		weatherGuideService.saveWeatherGuideDetail(weatherDetailRequest);
	}

	@GetMapping("/detail")
	public WeatherTipResponse getWeatherDetail(
		@RequestParam("weatherType")
		WeatherType weatherType
	) {
		return weatherGuideService.getWeatherTip(weatherType);
	}

	@PostMapping("/detail/accident/{weatherType}")
	public void saveWeatherAccidentDetail(
		@PathVariable(value = "weatherType")
		WeatherType weatherType
	) {
		weatherGuideService.saveWeatherAccidentCase(weatherType);
	}
}
