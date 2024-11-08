package com.woorifisa.wl.model.entity;

import com.woorifisa.wl.model.dto.FavoritePropertieDto;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "favorite_properties")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FavoritePropertie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "favorite_id", nullable = false)
    private Long favoriteId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "property_type", nullable = false, length = 50)
    private String propertyType;

    @Column(name = "apartment_name", length = 255)
    private String apartmentName;

    @Column(name = "address_si", length = 50)
    private String addressSi;

    @Column(name = "address_gu", length = 50)
    private String addressGu;

    @Column(name = "address_dong", length = 50)
    private String addressDong;

    @Column(name = "address_dongho", length = 50)
    private String addressDongho;

    @Column(name = "size_m2", precision = 10, scale = 2)
    private BigDecimal sizeM2;

    @Column(name = "price_latest", precision = 19, scale = 2)
    private BigDecimal priceLatest;

    @Column(name = "price_before_latest", precision = 19, scale = 2)
    private BigDecimal priceBeforeLatest;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Entity -> DTO로 데이터를 넘기기 위한 메서드
    public FavoritePropertieDto toDto() {
        return FavoritePropertieDto.builder()
                .favoriteId(this.favoriteId)
                .userId(this.userId)
                .propertyType(this.propertyType)
                .apartmentName(this.apartmentName)
                .addressSi(this.addressSi)
                .addressGu(this.addressGu)
                .addressDong(this.addressDong)
                .addressDongho(this.addressDongho)
                .sizeM2(this.sizeM2)
                .priceLatest(this.priceLatest)
                .priceBeforeLatest(this.priceBeforeLatest)
                .createdAt(this.createdAt)
                .build();
    }
}