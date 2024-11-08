package com.woorifisa.wl.model.entity;

import com.woorifisa.wl.model.dto.LoanInquiryHistoryDto;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "loan_inquiry_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanInquiryHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inquiry_id", nullable = false)
    private Long inquiryId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "bank_name", nullable = false, length = 255)
    private String bankName;

    @Column(name = "loan_name", nullable = false, length = 255)
    private String loanName;

    @Column(name = "product_name", length = 255)
    private String productName;

    @Column(name = "interest_rate_min", precision = 5, scale = 2)
    private BigDecimal interestRateMin;

    @Column(name = "interest_rate_max", precision = 5, scale = 2)
    private BigDecimal interestRateMax;

    @Column(name = "loan_limit", precision = 19, scale = 2)
    private BigDecimal loanLimit;

    @Column(name = "inquiry_date")
    private LocalDateTime inquiryDate;

    // Entity -> DTO로 데이터를 넘기기 위한 메서드
    public LoanInquiryHistoryDto toDto() {
        return LoanInquiryHistoryDto.builder()
                .inquiryId(this.inquiryId)
                .userId(this.userId)
                .bankName(this.bankName)
                .loanName(this.loanName)
                .productName(this.productName)
                .interestRateMin(this.interestRateMin)
                .interestRateMax(this.interestRateMax)
                .loanLimit(this.loanLimit)
                .inquiryDate(this.inquiryDate)
                .build();
    }
}