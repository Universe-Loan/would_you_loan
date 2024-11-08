package com.woorifisa.wl.model.dto;

import com.woorifisa.wl.model.entity.WooriLoan;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WooriLoanDto {

    private Long loanId;
    private Long userId;
    private String loanName;
    private BigDecimal loanAmount;
    private BigDecimal interestRate;
    private LocalDateTime loanStartDate;
    private LocalDateTime loanEndDate;
    private String loanStatus;
    private LocalDateTime createdAt;

    // DTO -> Entity로 데이터를 전송하는 toEntity() 메서드
    public WooriLoan toEntity() {
        WooriLoan wooriLoan = new WooriLoan();
        wooriLoan.setLoanId(this.getLoanId());
        wooriLoan.setUserId(this.getUserId());
        wooriLoan.setLoanName(this.getLoanName());
        wooriLoan.setLoanAmount(this.getLoanAmount());
        wooriLoan.setInterestRate(this.getInterestRate());
        wooriLoan.setLoanStartDate(this.getLoanStartDate());
        wooriLoan.setLoanEndDate(this.getLoanEndDate());
        wooriLoan.setLoanStatus(this.getLoanStatus());
        wooriLoan.setCreatedAt(this.getCreatedAt());
        return wooriLoan;
    }
}