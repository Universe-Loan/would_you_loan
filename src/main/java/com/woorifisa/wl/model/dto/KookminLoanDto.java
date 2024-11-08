package com.woorifisa.wl.model.dto;

import com.woorifisa.wl.model.entity.KookminLoan;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KookminLoanDto {

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
    public KookminLoan toEntity() {
        KookminLoan kookminLoan = new KookminLoan();
        kookminLoan.setLoanId(this.getLoanId());
        kookminLoan.setUserId(this.getUserId());
        kookminLoan.setLoanName(this.getLoanName());
        kookminLoan.setLoanAmount(this.getLoanAmount());
        kookminLoan.setInterestRate(this.getInterestRate());
        kookminLoan.setLoanStartDate(this.getLoanStartDate());
        kookminLoan.setLoanEndDate(this.getLoanEndDate());
        kookminLoan.setLoanStatus(this.getLoanStatus());
        kookminLoan.setCreatedAt(this.getCreatedAt());
        return kookminLoan;
    }
}