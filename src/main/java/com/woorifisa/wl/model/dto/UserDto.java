package com.woorifisa.wl.model.dto;

import com.woorifisa.wl.model.entity.User;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private Long userId;
    private String email;
    private String name;
    private LocalDateTime createdAt;
    private String isAdmin;

    // DTO -> Entity로 데이터를 전송하는 toEntity() 메서드
    public User toEntity() {
        User user = new User();
        user.setUserId(this.getUserId());
        user.setEmail(this.getEmail());
        user.setName(this.getName());
        user.setCreatedAt(this.getCreatedAt());
        user.setIsAdmin(this.getIsAdmin());
        return user;
    }
}