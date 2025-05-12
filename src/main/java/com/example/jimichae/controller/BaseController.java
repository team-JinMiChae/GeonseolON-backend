package com.example.jimichae.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.jimichae.dto.response.HealthResponse;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/v1")
public class BaseController {
	@GetMapping("/health")
	public HealthResponse health() {
		return new HealthResponse(HttpServletResponse.SC_OK);
	}

	@GetMapping("/ready")
	public String ready() {
		return "OK";
	}
}
