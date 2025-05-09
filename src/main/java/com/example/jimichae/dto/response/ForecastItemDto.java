package com.example.jimichae.dto.response;

import com.example.jimichae.entity.WeatherCategory;

public record ForecastItemDto(WeatherCategory category, String value){}
