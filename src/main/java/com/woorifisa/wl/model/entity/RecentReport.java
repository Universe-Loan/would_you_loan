package com.woorifisa.wl.model.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "recent_reports")
public class RecentReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recent_id")
    private Long recentId; // Primary Key

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "url")
    private String url;
}
