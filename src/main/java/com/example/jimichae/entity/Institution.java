package com.example.jimichae.entity;

import org.locationtech.jts.geom.Point;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.Getter;

@Entity
@Getter
@SequenceGenerator(name = "institution_seq", sequenceName = "institution_seq", allocationSize = 1)
public class Institution {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "institution_seq")
	Long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false, columnDefinition = "geometry(Point, 4326)")
	private Point geom;

	private String phoneNumber;

	//TODO : 속성 더 추가하기

	public Institution(Long id, String name, Point geom, String phoneNumber) {
		this.id = id;
		this.name = name;
		this.geom = geom;
		this.phoneNumber = phoneNumber;
	}

	public Institution() {
		this(0L, "", null,""); // TODO: null값이 맞나?
	}
}
