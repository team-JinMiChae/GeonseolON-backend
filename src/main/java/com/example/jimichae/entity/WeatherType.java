package com.example.jimichae.entity;

import lombok.Getter;

@Getter
public enum WeatherType {
	HOT("폭염인 날 "),
	CLEAR_DAY( "맑은 날 "),
	CLOUDY("흐린 날 "),
	SNOWY("눈 오는 날 "),
	RAIN("비 오는 날 "),
	LIGHTNING("번개 치는 날 "),
	WIND("바람 부는 날 "),
	RAIN_SNOW("비와 눈 오는 날 "),
	NO_DATA("데이터 없음");

	private final String description;

	WeatherType(String description) {
		this.description = description;
	}
}
