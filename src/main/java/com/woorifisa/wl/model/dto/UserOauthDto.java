package com.woorifisa.wl.model.dto;

import com.woorifisa.wl.model.entity.UserOauth;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserOauthDto {

    private Long oauthId;
    private Long userId;
    private String provider;
    private String providerId;
    private String accessToken;
    private String refreshToken;
    private LocalDateTime tokenExpiresAt;
    private LocalDateTime createdAt;

    // DTO -> Entity로 데이터를 전송하는 toEntity() 메서드
    public UserOauth toEntity() {
        UserOauth userOauth = new UserOauth();
        userOauth.setOauthId(this.getOauthId());
        userOauth.setUserId(this.getUserId());
        userOauth.setProvider(this.getProvider());
        userOauth.setProviderId(this.getProviderId());
        userOauth.setAccessToken(this.getAccessToken());
        userOauth.setRefreshToken(this.getRefreshToken());
        userOauth.setTokenExpiresAt(this.getTokenExpiresAt());
        userOauth.setCreatedAt(this.getCreatedAt());
        return userOauth;
    }
}