package com.example.jimichae.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties(prefix = "client.github")
@Getter
@Setter
public class GithubProperties {
	String token;
}
