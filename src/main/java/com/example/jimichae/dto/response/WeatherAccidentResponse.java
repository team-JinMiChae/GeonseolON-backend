package com.example.jimichae.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class WeatherAccidentResponse{
	private String title;
	private String description;
	private String fileUrl;
}
