package com.woorifisa.wl.model.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "apartment_pros")
public class ApartmentPros {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pros_id")
    private Long prosId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "kapt_code")
    private String kaptCode;

    @Column(name = "quiet")
    private Boolean quiet;

    @Column(name = "transport")
    private Boolean transport;

    @Column(name = "parking")
    private Boolean parking;

    @Column(name = "security")
    private Boolean security;

    @Column(name = "maintenance")
    private Boolean maintenance;

    @Column(name = "neighbors")
    private Boolean neighbors;

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