package com.woorifisa.wl.model.entity;

import com.woorifisa.wl.model.dto.ApartmentSaleDto;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "apartment_sales")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApartmentSale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sale_id", nullable = false)
    private Long saleId;

    @Column(name = "transaction_amount", nullable = false)
    private Long transactionAmount;

    @Column(name = "build_year")
    private Integer buildYear;

    @Column(name = "deal_year")
    private Integer dealYear;

    @Column(name = "deal_month")
    private Integer dealMonth;

    @Column(name = "deal_day")
    private Integer dealDay;

    @Column(name = "address_si", length = 50)
    private String addressSi;

    @Column(name = "address_gu", length = 50)
    private String addressGu;

    @Column(name = "address_dong", length = 50)
    private String addressDong;

    @Column(name = "address_dongho", length = 50)
    private String addressDongho;

    @Column(name = "apartment_name", length = 255)
    private String apartmentName;

    @Column(name = "area_m2", precision = 10, scale = 2)
    private BigDecimal areaM2;

    @Column(name = "jibun", length = 50)
    private String jibun;

    @Column(name = "region_code", length = 50)
    private String regionCode;

    @Column(name = "floor")
    private Integer floor;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Entity -> DTO로 데이터를 넘기기 위한 메서드
    public ApartmentSaleDto toDto() {
        return ApartmentSaleDto.builder()
                .saleId(this.saleId)
                .transactionAmount(this.transactionAmount)
                .buildYear(this.buildYear)
                .dealYear(this.dealYear)
                .dealMonth(this.dealMonth)
                .dealDay(this.dealDay)
                .addressSi(this.addressSi)
                .addressGu(this.addressGu)
                .addressDong(this.addressDong)
                .addressDongho(this.addressDongho)
                .apartmentName(this.apartmentName)
                .areaM2(this.areaM2)
                .jibun(this.jibun)
                .regionCode(this.regionCode)
                .floor(this.floor)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .build();
    }
}