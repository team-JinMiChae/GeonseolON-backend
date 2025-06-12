package com.example.jimichae.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.jimichae.entity.AccidentType;

@Repository
public interface AccidentTypeRepository extends JpaRepository<AccidentType, Long> {
	AccidentType findByBoardNo(int boardNo);

}
