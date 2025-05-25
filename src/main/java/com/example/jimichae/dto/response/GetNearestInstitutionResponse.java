package com.example.jimichae.dto.response;

import org.locationtech.jts.geom.Point;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GetNearestInstitutionResponse {
	long id;
	String name;
	double longitude;
	double latitude;
	String phoneNumber;
	String faxNumber;/**/
	String postalCode;
	String address;
	String region;
	double distance;

	public GetNearestInstitutionResponse(long id, String name, Point geom, String phoneNumber, double distance, String faxNumber, String postalCode, String address, String region) {
		this.id = id;
		this.name = name;
		this.longitude = geom.getX();
		this.latitude = geom.getY();
		this.phoneNumber = phoneNumber;
		this.distance = distance;
		this.faxNumber = faxNumber;
		this.postalCode = postalCode;
		this.address = address;
		this.region = region;
	}
}
