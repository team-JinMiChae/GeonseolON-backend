package com.example.jimichae.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {
	@GetMapping("/health")
	public String health() {
		return "OK";
	}

	@GetMapping("/ready")
	public String ready() {
		return "READY";
	}
}
