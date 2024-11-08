package com.woorifisa.wl.model.dto;

import com.woorifisa.wl.model.entity.FavoritePropertie;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FavoritePropertieDto {

    private Long favoriteId;
    private Long userId;
    private String propertyType;
    private String apartmentName;
    private String addressSi;
    private String addressGu;
    private String addressDong;
    private String addressDongho;
    private BigDecimal sizeM2;
    private BigDecimal priceLatest;
    private BigDecimal priceBeforeLatest;
    private LocalDateTime createdAt;

    // DTO -> Entity로 데이터를 전송하는 toEntity() 메서드
    public FavoritePropertie toEntity() {
        FavoritePropertie favoritePropertie = new FavoritePropertie();
        favoritePropertie.setFavoriteId(this.getFavoriteId());
        favoritePropertie.setUserId(this.getUserId());
        favoritePropertie.setPropertyType(this.getPropertyType());
        favoritePropertie.setApartmentName(this.getApartmentName());
        favoritePropertie.setAddressSi(this.getAddressSi());
        favoritePropertie.setAddressGu(this.getAddressGu());
        favoritePropertie.setAddressDong(this.getAddressDong());
        favoritePropertie.setAddressDongho(this.getAddressDongho());
        favoritePropertie.setSizeM2(this.getSizeM2());
        favoritePropertie.setPriceLatest(this.getPriceLatest());
        favoritePropertie.setPriceBeforeLatest(this.getPriceBeforeLatest());
        favoritePropertie.setCreatedAt(this.getCreatedAt());
        return favoritePropertie;
    }
}