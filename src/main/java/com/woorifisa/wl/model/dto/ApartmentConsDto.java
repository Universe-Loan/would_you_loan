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
}