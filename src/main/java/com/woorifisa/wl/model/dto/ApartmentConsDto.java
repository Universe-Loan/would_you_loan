package com.woorifisa.wl.model.dto;

import lombok.Data;

@Data
public class ApartmentConsDto {
    private String kaptCode;
    private long noiseCount;
    private long badTransportCount;
    private long badParkingCount;
    private long badSecurityCount;
    private long outdatedCount;
    private long badSchoolCount;
    private long badCommunityCount;
    private long badCommercialCount;
    private long badDaycareCount;
    private long badMedicalCount;
    private long badRestaurantsCount;
    private long badParksCount;

    // 모든 필드를 받는 생성자 추가
    public ApartmentConsDto(String kaptCode,
                            long noiseCount,
                            long badTransportCount,
                            long badParkingCount,
                            long badSecurityCount,
                            long outdatedCount,
                            long badSchoolCount,
                            long badCommunityCount,
                            long badCommercialCount,
                            long badDaycareCount,
                            long badMedicalCount,
                            long badRestaurantsCount,
                            long badParksCount) {
        this.kaptCode = kaptCode;
        this.noiseCount = noiseCount;
        this.badTransportCount = badTransportCount;
        this.badParkingCount = badParkingCount;
        this.badSecurityCount = badSecurityCount;
        this.outdatedCount = outdatedCount;
        this.badSchoolCount = badSchoolCount;
        this.badCommunityCount = badCommunityCount;
        this.badCommercialCount = badCommercialCount;
        this.badDaycareCount = badDaycareCount;
        this.badMedicalCount = badMedicalCount;
        this.badRestaurantsCount = badRestaurantsCount;
        this.badParksCount = badParksCount;
    }
}