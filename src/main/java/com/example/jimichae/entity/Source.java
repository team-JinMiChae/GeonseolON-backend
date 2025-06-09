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
import lombok.Setter;

@Entity
@Setter
@Getter
@SequenceGenerator(name = "source_seq", sequenceName = "source_seq", allocationSize = 1)
@NoArgsConstructor
@AllArgsConstructor
public class Source {
	@Id
	@GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "source_seq")
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false, length = 1000)
	private String url;
}
