package com.example.jimichae.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@SequenceGenerator(name = "threat_seq", sequenceName = "threat_seq", allocationSize = 1)
@AllArgsConstructor
@NoArgsConstructor
public class Threat {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "threat_seq")
	private Long id;

	@Column(nullable = false)
	private String name;
}
