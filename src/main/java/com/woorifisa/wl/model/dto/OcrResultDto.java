package com.woorifisa.wl.model.dto;

import lombok.Data;

@Data
public class OcrResultDto {
    private String location;        // 소재지
    private String buildingUse;     // 건물 용도
    private String exclusiveArea;   // 전용 면적
    private String salePrice;       // 매매 대금
    private String buyerAddress;    // 매수인 주소
    private String buyerName;       // 매수인 이름
}
