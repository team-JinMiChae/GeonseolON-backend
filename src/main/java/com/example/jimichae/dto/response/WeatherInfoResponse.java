package com.example.jimichae.dto.response;

import com.example.jimichae.entity.WeatherType;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WeatherInfoResponse{
	@Schema(description = "날씨 타입")
	WeatherType type;
	@Schema(description = "강수확률")
	String pop;
	@Schema(description = "습도")
	String reh;
	@Schema(description = "현재 기온")
	String tmp;
	@Schema(description = "일 최저기온")
	String tmn;
	@Schema(description = "일 최고기온")
	String tmx;
	@Schema(description = "예보 시간")
	String fcstTime;
	@Schema(description = "예보 날짜")
	String fcstDate;
	@Schema(description = "날씨에 따른 멘트")
	String weatherMent;
}
