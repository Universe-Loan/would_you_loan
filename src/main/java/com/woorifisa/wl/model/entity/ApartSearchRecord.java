package com.woorifisa.wl.model.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "apart_search_record")
public class ApartSearchRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "url_id")
    private Long urlId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "city")
    private String city;

    @Column(name = "district")
    private String district;

    @Column(name = "apart_name")
    private String apartName;

    @Column(name = "url")
    private String url;
}
