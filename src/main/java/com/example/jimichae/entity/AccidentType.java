package com.example.jimichae.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccidentType {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private int boardNo;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 100)
	private Accident accident;

	public AccidentType(int boardNo, Accident accident) {
		this.id = null;
		this.boardNo = boardNo;
		this.accident = accident;
	}
}
