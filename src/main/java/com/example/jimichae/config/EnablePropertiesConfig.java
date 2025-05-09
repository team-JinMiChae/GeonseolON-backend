package com.example.jimichae.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@EnableConfigurationProperties({
	GithubProperties.class,
	AccidentCaseProperties.class, // AccidentCaseProperties를 활성화
	WeatherGuideProperties.class // WeatherGuideProperties를 활성화
})
@Configuration
public class EnablePropertiesConfig {
}
