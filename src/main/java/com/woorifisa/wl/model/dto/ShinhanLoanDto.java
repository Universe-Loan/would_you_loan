package com.woorifisa.wl.model.dto;

import com.woorifisa.wl.model.entity.ShinhanLoan;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShinhanLoanDto {

    private Long loanId;
    private Long userId;
    private String loanName;
    private BigDecimal loanAmount;
    private BigDecimal interestRate;
    private LocalDateTime loanStartDate;
    private LocalDateTime loanEndDate;
    private String loanStatus;
    private LocalDateTime createdAt;

    public ShinhanLoan toEntity() {
        ShinhanLoan shinhanLoan = new ShinhanLoan();
        shinhanLoan.setLoanId(this.getLoanId());
        shinhanLoan.setUserId(this.getUserId());
        shinhanLoan.setLoanName(this.getLoanName());
        shinhanLoan.setLoanAmount(this.getLoanAmount());
        shinhanLoan.setInterestRate(this.getInterestRate());
        shinhanLoan.setLoanStartDate(this.getLoanStartDate());
        shinhanLoan.setLoanEndDate(this.getLoanEndDate());
        shinhanLoan.setLoanStatus(this.getLoanStatus());
        shinhanLoan.setCreatedAt(this.getCreatedAt());
        return shinhanLoan;
    }
}