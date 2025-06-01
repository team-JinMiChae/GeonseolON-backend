package com.example.jimichae.config;

import static com.example.jimichae.util.Constants.NO_CLIENT_URLS;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig {
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.cors(cors -> cors.configurationSource(corsConfigurationSource())) // CORS 설정
			.csrf(AbstractHttpConfigurer::disable) // CSRF 비활성화
			.authorizeHttpRequests(auth -> auth
				.requestMatchers(NO_CLIENT_URLS.toArray(new String[0])).denyAll() // NO_CLIENT_URLS 경로 차단
				.anyRequest().permitAll() // 나머지 요청 허용
			);
		return http.build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration corsConfig = new CorsConfiguration();
		corsConfig.setAllowedOriginPatterns(List.of("http://localhost:8080", "http://localhost:5173","http://localhost:3000", "http://localhost:4173 ","https://geonseol-on-frontend-n5xk.vercel.app", "https://jinmichae.jayden-bin.cc", "*"));
		corsConfig.setAllowedMethods(List.of("GET", "POST", "PATCH", "DELETE", "PUT", "OPTIONS"));
		corsConfig.setAllowedHeaders(List.of("Authorization", "Content-Type"));

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", corsConfig);
		return source;
	}
}
