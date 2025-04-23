package com.example.jimichae.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.jimichae.entity.AccidentCase;

@Repository
public interface AccidentCaseRepository extends JpaRepository<AccidentCase,Long> {
	boolean existsAccidentCaseByTheVector(Float[] theVector);
}
