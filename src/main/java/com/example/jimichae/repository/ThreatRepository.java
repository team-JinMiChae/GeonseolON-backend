package com.example.jimichae.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.jimichae.entity.Threat;

@Repository
public interface ThreatRepository extends JpaRepository<Threat,Long> {
	boolean existsByName(String name);
	Threat findByName(String name);
}
