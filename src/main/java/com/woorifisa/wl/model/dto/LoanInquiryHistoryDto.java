package com.woorifisa.wl.model.dto;

import com.woorifisa.wl.model.entity.LoanInquiryHistory;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanInquiryHistoryDto {

    private Long inquiryId;
    private Long userId;
    private String bankName;
    private String loanName;
    private String productName;
    private BigDecimal interestRateMin;
    private BigDecimal interestRateMax;
    private BigDecimal loanLimit;
    private LocalDateTime inquiryDate;

    // DTO -> Entity로 데이터를 전송하는 toEntity() 메서드
    public LoanInquiryHistory toEntity() {
        LoanInquiryHistory loanInquiryHistory = new LoanInquiryHistory();
        loanInquiryHistory.setInquiryId(this.getInquiryId());
        loanInquiryHistory.setUserId(this.getUserId());
        loanInquiryHistory.setBankName(this.getBankName());
        loanInquiryHistory.setLoanName(this.getLoanName());
        loanInquiryHistory.setProductName(this.getProductName());
        loanInquiryHistory.setInterestRateMin(this.getInterestRateMin());
        loanInquiryHistory.setInterestRateMax(this.getInterestRateMax());
        loanInquiryHistory.setLoanLimit(this.getLoanLimit());
        loanInquiryHistory.setInquiryDate(this.getInquiryDate());
        return loanInquiryHistory;
    }
}