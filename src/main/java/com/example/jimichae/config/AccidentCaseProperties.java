package com.example.jimichae.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties(prefix = "client.accident-case") // application.yml의 client.accident-case에 매핑
@Getter
@Setter
public class AccidentCaseProperties {
String apiKey;
String embeddingKey;
String embeddingUrl;
}
