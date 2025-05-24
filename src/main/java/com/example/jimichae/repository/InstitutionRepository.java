package com.example.jimichae.repository;

import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.jimichae.dto.response.GetNearestInstitutionResponse;
import com.example.jimichae.entity.Institution;

@Repository
public interface InstitutionRepository extends JpaRepository<Institution, Long> {
	@Query("SELECT new com.example.jimichae.dto.response.GetNearestInstitutionResponse(i.id, i.name, i.geom, i.phoneNumber, ST_Distance(i.geom, :point), i.faxNumber, i.postalCode, i.address, i.region) FROM Institution i "
		+"WHERE (:lastId IS NULL OR ST_Distance(i.geom, :point) > (SELECT ST_Distance(i2.geom, :point) FROM Institution i2 WHERE i2.id = :lastId)) "
		+ "ORDER BY ST_Distance(i.geom, :point) ASC")
	Slice<GetNearestInstitutionResponse> findNearestInstitution(Point point, Pageable pageable,Long lastId);

	boolean existsByRegistrationNumber(int registrationNumber);
}
