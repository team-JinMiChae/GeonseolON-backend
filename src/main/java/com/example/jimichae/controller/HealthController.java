package com.example.jimichae.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {
	@GetMapping("/api/v1/health")
	public String health() {
		return "OK";
	}

	@GetMapping("/api/v1/ready")
	public String ready() {
		return "READY";
	}
}
