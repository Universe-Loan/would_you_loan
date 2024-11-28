package com.woorifisa.wl.model.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "loan_search_record")
public class LoanSearchRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "url_id")
    private Long urlId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "apart_name")
    private String apartName;

    @Column(name = "url")
    private String url;
}
