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
}