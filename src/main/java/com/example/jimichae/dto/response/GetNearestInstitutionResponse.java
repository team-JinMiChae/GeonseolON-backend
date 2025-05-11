package com.example.jimichae.dto.response;

import org.locationtech.jts.geom.Point;
public record GetNearestInstitutionResponse(
	long id,
	String name,
	Point geom,
	String phoneNumber,
	double distance
){}
