package com.example.jimichae.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.jimichae.entity.Source;

@Repository
public interface SourceRepository extends JpaRepository<Source,Long> {
	boolean existsByNameAndUrl(String name, String url);
	Source findByNameAndUrl(String name, String url);
}
