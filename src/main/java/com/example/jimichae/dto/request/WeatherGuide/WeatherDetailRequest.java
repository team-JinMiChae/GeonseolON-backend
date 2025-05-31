package com.example.jimichae.dto.request.WeatherGuide;

import java.util.List;

import com.example.jimichae.entity.WeatherType;

import jakarta.annotation.Nullable;

public record WeatherDetailRequest(
	WeatherType type,
	String simpleSafetyMeasures,
	String detailedSafetyMeasures,
	@Nullable
	List<SourcesRequest> sources,
	@Nullable
	List<ThreatRequest> threatRequests,
	@Nullable
	String weatherSafetyMeasures
){}
