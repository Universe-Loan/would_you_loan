package com.woorifisa.wl.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name="name")
    private String name;

    @Column(name="email")
    private String email;

    @Column(name="oauth_type")
    private String oauthType;

    @Column(name="created_at")
    private LocalDateTime createdAt;

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt; // 최근 로그인 시간

    @Enumerated(EnumType.STRING) // Enum 매핑 설정
    @Column(name="role")
    @ColumnDefault("'GUEST'")
    private Role role;
}