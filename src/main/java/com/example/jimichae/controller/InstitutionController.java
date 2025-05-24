package com.example.jimichae.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.jimichae.dto.response.SliceResponse;
import com.example.jimichae.service.InstitutionService;

@RestController
@RequestMapping("/api/v1/institution")
public class InstitutionController {
	private final InstitutionService institutionService;

	public InstitutionController(InstitutionService institutionService) {
		this.institutionService = institutionService;
	}
	@GetMapping
	public SliceResponse getInstitutionDetails(
		@RequestParam("longitude") double longitude,
		@RequestParam("latitude") double latitude,
		@RequestParam(name = "lastId", required = false) Long lastId
	) {
		return institutionService.getNearestInstitution(longitude, latitude, lastId);
	}

	@PostMapping("/save")
	public void saveInstitutionDetails(
		@RequestParam("filePath") String filePath
	){
		institutionService.saveInstitutionCoordinates(filePath);
	}
}
