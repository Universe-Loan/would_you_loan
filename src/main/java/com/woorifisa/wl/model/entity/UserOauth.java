package com.woorifisa.wl.model.entity;

import com.woorifisa.wl.model.dto.UserOauthDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_oauth")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserOauth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "oauth_id", nullable = false)
    private Long oauthId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "provider", nullable = false, length = 50)
    private String provider;

    @Column(name = "provider_id", nullable = false, length = 255)
    private String providerId;

    @Column(name = "access_token", nullable = false)
    private String accessToken;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "token_expires_at")
    private LocalDateTime tokenExpiresAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Entity -> DTO로 데이터를 넘기기 위한 메서드
    public UserOauthDto toDto() {
        return UserOauthDto.builder()
                .oauthId(this.oauthId)
                .userId(this.userId)
                .provider(this.provider)
                .providerId(this.providerId)
                .accessToken(this.accessToken)
                .refreshToken(this.refreshToken)
                .tokenExpiresAt(this.tokenExpiresAt)
                .createdAt(this.createdAt)
                .build();
    }
}