package com.woorifisa.wl.model.dto;

import com.woorifisa.wl.model.entity.MyPropertie;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyPropertieDto {

    private Long propertyId;
    private Long userId;
    private String propertyType;
    private String propertyName;
    private String addressSi;
    private String addressGu;
    private String addressDong;
    private String addressDongho;
    private BigDecimal sizeM2;
    private LocalDateTime purchaseDate;
    private BigDecimal purchasePrice;
    private BigDecimal currentEstimatedPrice;
    private String registrationNumber;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // DTO -> Entity로 데이터를 전송하는 toEntity() 메서드
    public MyPropertie toEntity() {
        MyPropertie myPropertie = new MyPropertie();
        myPropertie.setPropertyId(this.getPropertyId());
        myPropertie.setUserId(this.getUserId());
        myPropertie.setPropertyType(this.getPropertyType());
        myPropertie.setPropertyName(this.getPropertyName());
        myPropertie.setAddressSi(this.getAddressSi());
        myPropertie.setAddressGu(this.getAddressGu());
        myPropertie.setAddressDong(this.getAddressDong());
        myPropertie.setAddressDongho(this.getAddressDongho());
        myPropertie.setSizeM2(this.getSizeM2());
        myPropertie.setPurchaseDate(this.getPurchaseDate());
        myPropertie.setPurchasePrice(this.getPurchasePrice());
        myPropertie.setCurrentEstimatedPrice(this.getCurrentEstimatedPrice());
        myPropertie.setRegistrationNumber(this.getRegistrationNumber());
        myPropertie.setCreatedAt(this.getCreatedAt());
        myPropertie.setUpdatedAt(this.getUpdatedAt());
        return myPropertie;
    }
}