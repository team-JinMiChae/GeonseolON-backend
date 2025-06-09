package com.example.jimichae.dto.request.WeatherGuide;

import jakarta.annotation.Nullable;

public record ThreatRequest(
	String name,
	@Nullable
	String safetyMeasures) {}
