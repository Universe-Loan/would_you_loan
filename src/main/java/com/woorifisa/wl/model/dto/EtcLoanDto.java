package com.woorifisa.wl.model.dto;

import com.woorifisa.wl.model.entity.EtcLoan;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EtcLoanDto {

    private Long loanId;
    private Long userId;
    private String loanName;
    private String bankName;
    private BigDecimal loanAmount;
    private BigDecimal interestRate;
    private LocalDateTime loanStartDate;
    private LocalDateTime loanEndDate;
    private String loanStatus;
    private LocalDateTime createdAt;

    // DTO -> Entity로 데이터를 전송하는 toEntity() 메서드
    public EtcLoan toEntity() {
        EtcLoan etcLoan = new EtcLoan();
        etcLoan.setLoanId(this.getLoanId());
        etcLoan.setUserId(this.getUserId());
        etcLoan.setLoanName(this.getLoanName());
        etcLoan.setBankName(this.getBankName());
        etcLoan.setLoanAmount(this.getLoanAmount());
        etcLoan.setInterestRate(this.getInterestRate());
        etcLoan.setLoanStartDate(this.getLoanStartDate());
        etcLoan.setLoanEndDate(this.getLoanEndDate());
        etcLoan.setLoanStatus(this.getLoanStatus());
        etcLoan.setCreatedAt(this.getCreatedAt());
        return etcLoan;
    }
}