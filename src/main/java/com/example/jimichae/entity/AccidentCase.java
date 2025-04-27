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
import lombok.Setter;

@Entity
@Getter
@Setter
@SequenceGenerator(name = "accident_case_seq", sequenceName = "accident_case_seq", allocationSize = 1)
public class AccidentCase{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY, generator = "accident_case_seq")
    Long id;
    @Column(nullable = false)
    @JdbcTypeCode(SqlTypes.VECTOR)
    @Array(length = 3072)
    Float[] theVector;
    @Column(nullable = false)
    @Lob
    String originalText;
    @Column(nullable = false)
    int boardNo;

    public AccidentCase(Long id, Float[] theVector, String originalText, int boardNo) {
        this.id = id;
        this.theVector = theVector;
        this.originalText = originalText;
        this.boardNo = boardNo;
    }

    public  AccidentCase() {this(0L, new Float[0], "", -1);}
}
