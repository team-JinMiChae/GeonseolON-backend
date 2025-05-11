package com.example.jimichae.service;

import java.util.List;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

import com.example.jimichae.dto.response.GetNearestInstitutionResponse;
import com.example.jimichae.entity.Institution;
import com.example.jimichae.repository.InstitutionRepository;

@Service
public class InstitutionService {
	private final InstitutionRepository institutionRepository;
	private final GeometryFactory geometryFactory = new GeometryFactory();

	public InstitutionService(InstitutionRepository institutionRepository) {
		this.institutionRepository = institutionRepository;
	}
	public void saveInstitutionCoordinates() {
		String name = "" ;
		double longitude = 0.0;
		double latitude = 0.0; //TODO : 좌표값과 이름을 어떻게 가져올지 고민하기
		String phoneNumber = "" ;
		Point point = geometryFactory.createPoint(new Coordinate(longitude, latitude));
		point.setSRID(4326); // 중요! 좌표계 지정
		institutionRepository.save(new Institution(0L,name,point,phoneNumber));
	}
	
	public List<GetNearestInstitutionResponse> getNearestInstitution(double longitude, double latitude) {
		Point point = geometryFactory.createPoint(new Coordinate(longitude, latitude));
		point.setSRID(4326);
		return institutionRepository.findNearestInstitution(point);
	}
}
