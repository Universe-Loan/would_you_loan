package com.woorifisa.wl.model.dto;

import lombok.Data;

@Data
public class AssetRecordDto {
    private Long recentId;
    private Long userId;
    private String assetType;
    private Long amount;
    private Integer year;
    private Integer month;
}