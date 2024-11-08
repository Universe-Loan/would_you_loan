package com.woorifisa.wl.model.entity;

import com.woorifisa.wl.model.dto.MyPropertieDto;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "my_properties")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyPropertie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "property_id", nullable = false)
    private Long propertyId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "property_type", nullable = false, length = 50)
    private String propertyType;

    @Column(name = "property_name", length = 255)
    private String propertyName;

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

    @Column(name = "purchase_date")
    private LocalDateTime purchaseDate;

    @Column(name = "purchase_price", precision = 19, scale = 2)
    private BigDecimal purchasePrice;

    @Column(name = "current_estimated_price", precision = 19, scale = 2)
    private BigDecimal currentEstimatedPrice;

    @Column(name = "registration_number", length = 50)
    private String registrationNumber;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Entity -> DTO로 데이터를 넘기기 위한 메서드
    public MyPropertieDto toDto() {
        return MyPropertieDto.builder()
                .propertyId(this.propertyId)
                .userId(this.userId)
                .propertyType(this.propertyType)
                .propertyName(this.propertyName)
                .addressSi(this.addressSi)
                .addressGu(this.addressGu)
                .addressDong(this.addressDong)
                .addressDongho(this.addressDongho)
                .sizeM2(this.sizeM2)
                .purchaseDate(this.purchaseDate)
                .purchasePrice(this.purchasePrice)
                .currentEstimatedPrice(this.currentEstimatedPrice)
                .registrationNumber(this.registrationNumber)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .build();
    }
}