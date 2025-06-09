package com.example.jimichae.dto.response;

import java.util.List;

import com.example.jimichae.entity.WeatherType;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class WeatherTipResponse {
	private WeatherType weatherType;
	private String simpleTip;
	private String detailedTip;
	private List<String> commonMeasure;
	private List<WeatherThreatResponse> weatherThreats;
	private List<WeatherTipSourceResponse> sources;
}
