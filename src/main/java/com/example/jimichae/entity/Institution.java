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

@Entity
@Getter
@AllArgsConstructor
@SequenceGenerator(name = "institution_seq", sequenceName = "institution_seq", allocationSize = 1, initialValue = 1)
public class Institution {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "institution_seq")
	Long id;

	@Column(nullable = false)
	String name;

	@Column(nullable = false, columnDefinition = "geometry(Point, 4326)")
	Point geom;

	String phoneNumber;

	//TODO : 속성 더 추가하기
	public Institution() {
		this(null, "", null,""); // TODO: null값이 맞나?
	}
}
