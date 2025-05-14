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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@SequenceGenerator(name = "accident_case_seq", sequenceName = "accident_case_seq", allocationSize = 1, initialValue = 1)
@AllArgsConstructor
public class AccidentCase{
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "accident_case_seq")
    Long id;
    @Column(nullable = false, columnDefinition = "VECTOR(3072)")
    @JdbcTypeCode(SqlTypes.VECTOR_FLOAT32)
    @Array(length = 3072)
    float[] theVector;
    @Column(nullable = false, columnDefinition = "CLOB")
    @Lob
    String originalText;
    @Column(nullable = false)
    int boardNo;
    @Column(nullable = false)
    String keyword;

    public AccidentCase() {
        this.id = null;
        this.theVector = new float[3072];
        this.originalText = "";
        this.boardNo = 0;
        this.keyword = "";
    }
}
