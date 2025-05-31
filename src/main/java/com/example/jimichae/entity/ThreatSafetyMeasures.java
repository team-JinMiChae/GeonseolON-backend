package com.example.jimichae.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SequenceGenerator(name = "threat_safety_measures_seq", sequenceName = "threat_safety_measures_seq", allocationSize = 1, initialValue = 1)
public class ThreatSafetyMeasures {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "threat_safety_measures_seq")
	private Long id;

	@ManyToOne
	private Threat threat;

	@Column(nullable = false, columnDefinition = "CLOB")
	@Lob
	private String safetyMeasures;
}
