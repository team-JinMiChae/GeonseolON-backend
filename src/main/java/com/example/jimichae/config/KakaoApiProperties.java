package com.example.jimichae.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties(prefix = "client.kakao")
@Getter
@Setter
public class KakaoApiProperties {
	String restApiKey;
}
