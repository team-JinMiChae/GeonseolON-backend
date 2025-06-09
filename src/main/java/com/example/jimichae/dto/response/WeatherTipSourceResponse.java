package com.example.jimichae.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class WeatherTipSourceResponse {
	private String title;
	private String url;
}
