package com.woorifisa.wl.model.entity;

import com.woorifisa.wl.model.dto.UserDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "email", nullable = false, length = 255)
    private String email;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "is_admin", nullable = false, length = 5)
    private String isAdmin;

    // Entity -> DTO로 데이터를 넘기기 위한 메서드
    public UserDto toDto() {
        return UserDto.builder()
                .userId(this.userId)
                .email(this.email)
                .name(this.name)
                .createdAt(this.createdAt)
                .isAdmin(this.isAdmin)
                .build();
    }
}