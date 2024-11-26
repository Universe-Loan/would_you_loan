package com.woorifisa.wl.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
@Table(name = "asset_records")
public class AssetRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id")
    private Long recentId; // Primary Key

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "asset_type")
    private String assetType;

    @Column(name = "amount")
    private Long amount;

    @Column(name = "year")
    private Integer year;

    @Column(name = "month")
    private Integer month;

}
