package com.woorifisa.wl.model.entity;

import com.woorifisa.wl.model.dto.WooriLoanDto;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "woori_loans")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WooriLoan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "loan_id", nullable = false)
    private Long loanId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "loan_name", nullable = false, length = 255)
    private String loanName;

    @Column(name = "loan_amount", precision = 19, scale = 2)
    private BigDecimal loanAmount;

    @Column(name = "interest_rate", precision = 5, scale = 2)
    private BigDecimal interestRate;

    @Column(name = "loan_start_date")
    private LocalDateTime loanStartDate;

    @Column(name = "loan_end_date")
    private LocalDateTime loanEndDate;

    @Column(name = "loan_status", nullable = false, length = 50)
    private String loanStatus;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Entity -> DTO로 데이터를 넘기기 위한 메서드
    public WooriLoanDto toDto() {
        return WooriLoanDto.builder()
                .loanId(this.loanId)
                .userId(this.userId)
                .loanName(this.loanName)
                .loanAmount(this.loanAmount)
                .interestRate(this.interestRate)
                .loanStartDate(this.loanStartDate)
                .loanEndDate(this.loanEndDate)
                .loanStatus(this.loanStatus)
                .createdAt(this.createdAt)
                .build();
    }
}