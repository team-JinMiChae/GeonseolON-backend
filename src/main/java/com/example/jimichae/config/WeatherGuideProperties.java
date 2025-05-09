package com.example.jimichae.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;


@ConfigurationProperties(prefix = "client.weather-guide") // application.yml의 client.weather-guide에 매핑
@Getter
@Setter
public class WeatherGuideProperties {
	String apiKey;
}
