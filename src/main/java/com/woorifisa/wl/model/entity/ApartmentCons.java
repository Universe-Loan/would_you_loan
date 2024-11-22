package com.woorifisa.wl.model.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "apartment_cons")
public class ApartmentCons {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cons_id")
    private Long consId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "kapt_code")
    private String kaptCode;

    @Column(name = "noise")
    private Boolean noise;

    @Column(name = "transport")
    private Boolean transport;

    @Column(name = "parking")
    private Boolean parking;

    @Column(name = "security")
    private Boolean security;

    @Column(name = "outdated")
    private Boolean outdated;

    @Column(name = "school")
    private Boolean school;

    @Column(name = "community")
    private Boolean community;

    @Column(name = "commercial")
    private Boolean commercial;

    @Column(name = "daycare")
    private Boolean daycare;

    @Column(name = "medical")
    private Boolean medical;

    @Column(name = "restaurants")
    private Boolean restaurants;

    @Column(name = "parks")
    private Boolean parks;
}
