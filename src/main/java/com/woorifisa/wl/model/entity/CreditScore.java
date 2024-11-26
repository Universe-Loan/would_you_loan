package com.woorifisa.wl.model.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "credit_scores")
public class CreditScore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "score_id")
    private Long scoreId; // Primary Key

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "score")
    private Long score;

    @Column(name="grade")
    private Integer grade;

    @Column(name = "year")
    private Integer year;

    @Column(name = "quarter")
    private Integer quarter;
}
