package com.example.jimichae.entity;

import lombok.Getter;

@Getter
public enum WeatherCategory {
	POP("강수확률", "%"),
	PTY("강수형태", "코드"),
	PCP("1시간 강수량", "mm"),
	SNO("1시간 신적설", "cm"),
	REH("습도", "%"),
	SKY("하늘 상태", "코드"),
	TMP("현재기온", "℃"),
	TMN("일 최저기온", "℃"),
	TMX("일 최고기온", "℃"),
	WSD("풍속", "m/s");
	private final String unit;
	WeatherCategory(String description, String unit) {
		this.unit = unit;
	}
}
