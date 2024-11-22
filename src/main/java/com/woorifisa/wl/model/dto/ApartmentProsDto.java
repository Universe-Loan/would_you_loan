package com.woorifisa.wl.model.dto;

import lombok.Data;

@Data
public class ApartmentProsDto {
    private String kaptCode;
    private long quietCount;
    private long transportCount;
    private long parkingCount;
    private long securityCount;
    private long maintenanceCount;
    private long neighborsCount;
    private long schoolCount;
    private long communityCount;
    private long commercialCount;
    private long daycareCount;
    private long medicalCount;
    private long restaurantsCount;
    private long parksCount;

    // 모든 필드를 받는 생성자 추가
    public ApartmentProsDto(String kaptCode,
                            long quietCount,
                            long transportCount,
                            long parkingCount,
                            long securityCount,
                            long maintenanceCount,
                            long neighborsCount,
                            long schoolCount,
                            long communityCount,
                            long commercialCount,
                            long daycareCount,
                            long medicalCount,
                            long restaurantsCount,
                            long parksCount) {
        this.kaptCode = kaptCode;
        this.quietCount = quietCount;
        this.transportCount = transportCount;
        this.parkingCount = parkingCount;
        this.securityCount = securityCount;
        this.maintenanceCount = maintenanceCount;
        this.neighborsCount = neighborsCount;
        this.schoolCount = schoolCount;
        this.communityCount = communityCount;
        this.commercialCount = commercialCount;
        this.daycareCount = daycareCount;
        this.medicalCount = medicalCount;
        this.restaurantsCount = restaurantsCount;
        this.parksCount = parksCount;
    }
}