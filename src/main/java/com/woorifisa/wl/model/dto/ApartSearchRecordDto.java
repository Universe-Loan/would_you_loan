package com.woorifisa.wl.model.dto;

import lombok.Data;

@Data
public class ApartSearchRecordDto {
    private Long urlId;
    private Long userId;
    private String city;
    private String district;
    private String apartName;
    private String url;
}
