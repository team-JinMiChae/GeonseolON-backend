package com.example.jimichae.dto.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ForecastTimeGroupDto {
	private String fcstTime;
	private List<ForecastItemDto> items;
}
