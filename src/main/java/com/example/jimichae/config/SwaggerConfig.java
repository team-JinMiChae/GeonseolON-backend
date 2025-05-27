package com.example.jimichae.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {
	@Bean
	public OpenAPI openAPI() {
		return new OpenAPI()
			.components(new Components())
			.info(new Info()
				.title("JINMICHAE API")
				.description("JINMICHAE API 명세서")
				.version("v1.0.0"))
			.servers(List.of(
			new Server()
				.url("http://localhost:8080")
				.description("Local Server"),
			new Server()
				.url("https://jinmichae.jayden-bin.cc")
				.description("Remote Server")));
	}
}
