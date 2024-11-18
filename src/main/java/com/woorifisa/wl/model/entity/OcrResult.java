package com.woorifisa.wl.model.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "ocr_results")
public class OcrResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ocr_result_id")
    private Long ocrResultId;

    private String location;        // 소재지

    @Column(name = "building_use") // 매핑 명시적으로 지정
    private String buildingUse;     // 건물 용도

    @Column(name = "exclusive_area")
    private String exclusiveArea;   // 전용 면적

    @Column(name = "sale_price")
    private String salePrice;       // 매매 대금

    @Column(name = "buyer_address")
    private String buyerAddress;    // 매수인 주소

    @Column(name = "buyer_name")
    private String buyerName;       // 매수인 이름
}
