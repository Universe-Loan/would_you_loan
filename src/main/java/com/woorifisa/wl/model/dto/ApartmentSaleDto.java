package com.woorifisa.wl.model.dto;

import com.woorifisa.wl.model.entity.ApartmentSale;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApartmentSaleDto {

    private Long saleId;
    private Long transactionAmount;
    private Integer buildYear;
    private Integer dealYear;
    private Integer dealMonth;
    private Integer dealDay;
    private String addressSi;
    private String addressGu;
    private String addressDong;
    private String addressDongho;
    private String apartmentName;
    private BigDecimal areaM2;
    private String jibun;
    private String regionCode;
    private Integer floor;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // DTO -> Entity로 데이터를 전송하는 toEntity() 메서드
    public ApartmentSale toEntity() {
        ApartmentSale apartmentSale = new ApartmentSale();
        apartmentSale.setSaleId(this.getSaleId());
        apartmentSale.setTransactionAmount(this.getTransactionAmount());
        apartmentSale.setBuildYear(this.getBuildYear());
        apartmentSale.setDealYear(this.getDealYear());
        apartmentSale.setDealMonth(this.getDealMonth());
        apartmentSale.setDealDay(this.getDealDay());
        apartmentSale.setAddressSi(this.getAddressSi());
        apartmentSale.setAddressGu(this.getAddressGu());
        apartmentSale.setAddressDong(this.getAddressDong());
        apartmentSale.setAddressDongho(this.getAddressDongho());
        apartmentSale.setApartmentName(this.getApartmentName());
        apartmentSale.setAreaM2(this.getAreaM2());
        apartmentSale.setJibun(this.getJibun());
        apartmentSale.setRegionCode(this.getRegionCode());
        apartmentSale.setFloor(this.getFloor());
        apartmentSale.setCreatedAt(this.getCreatedAt());
        apartmentSale.setUpdatedAt(this.getUpdatedAt());
        return apartmentSale;
    }
}