package com.woorifisa.wl.model.dto;

import lombok.Data;

@Data
public class LoanSearchDto {
    // 주소 정보
    private String city;
    private String district;
    private String neighborhood;
    private String apartment;
    private String lawdCd;

    // 아파트 및 대출 금액 정보
    private String apartmentPrice;  // 추가된 아파트 가격 필드
    private String loanAmount;
    private String loanTerm;

    // 조건 정보
    private String loanPurpose;
    private String firstTimeBuyer;
    private String loanType;

    // 신용등급 추가
    private Integer creditRating;
}