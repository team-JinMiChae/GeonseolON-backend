package com.example.jimichae.entity;

import org.hibernate.annotations.Array;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.SequenceGenerator;
import lombok.Getter;

@Entity
@Getter
@SequenceGenerator(name = "accident_case_seq", sequenceName = "accident_case_seq", allocationSize = 1)
public class AccidentCase{
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    Long id;
    @Column
    @JdbcTypeCode(SqlTypes.VECTOR)
    @Array(length = 1536)
    Float[] theVector;
    @Column()
    @Lob
    String originalText;

    public AccidentCase(Long id, Float[] theVector, String originalText) {
        this.id = id;
        this.theVector = theVector;
        this.originalText = originalText;
    }

    public  AccidentCase() {this(0L, new Float[0], "");}
}
