package com.example.jimichae.repository;

import java.util.List;

import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.jimichae.dto.response.GetNearestInstitutionResponse;
import com.example.jimichae.entity.Institution;

@Repository
public interface InstitutionRepository extends JpaRepository<Institution, Long> {
	// @Query(value = "SELECT * FROM institution ORDER BY geom <-> ?1 LIMIT 1", nativeQuery = true)
	@Query(value = "select  i.id, i.name, i.geom, i.phoneNumber, ST_Distance(i.geom, :point) as distance from Institution i order by ST_Distance(i.geom, :point) limit 5")
	List<GetNearestInstitutionResponse> findNearestInstitution(Point point);
}
