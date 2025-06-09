package com.example.jimichae.entity;

import org.locationtech.jts.geom.Point;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@SequenceGenerator(name = "institution_seq", sequenceName = "institution_seq", allocationSize = 1)
public class Institution {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "institution_seq")
	Long id;

	@Column(nullable = false)
	String name;

	@Column(columnDefinition = "SDO_GEOMETRY", nullable = false)
	Point geom;

	@Column(nullable = false)
	String phoneNumber;

	@Column(nullable = false)
	String faxNumber;

	@Column(nullable = false)
	String postalCode;

	@Column(nullable = false)
	String address;

	@Column(nullable = false)
	String region;

	@Column(nullable = false, unique = true)
	int registrationNumber;
}
