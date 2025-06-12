package com.example.jimichae.entity;

import lombok.Getter;

@Getter
public enum Accident {
	FALLING( "추락사고"),
	ELECTRIC_SHOCK( "감전사고"),
	FLOODING("침수사고"),
	SOIL_COLLAPSE("토사붕괴사고"),
	HEAT_STROKE( "열사병사고"),
	POISONING_AND_SUFFOCATION("중독 및 질식사고"),
	SCAFFOLD_COLLAPSE("비계붕괴사고"),
	FALLING_OBJECT("낙하물 사고"),
	OTHER("기타 사고");
	private final String description;

	Accident(String description) {
		this.description = description;
	}
}
