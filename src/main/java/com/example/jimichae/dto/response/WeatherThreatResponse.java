package com.example.jimichae.dto.response;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class WeatherThreatResponse {
	private String type;
	private List<String> measure;
	private List<WeatherAccidentResponse> weatherAccidents;
}
