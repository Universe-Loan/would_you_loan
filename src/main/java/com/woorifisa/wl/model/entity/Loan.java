package com.woorifisa.wl.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
@Table(name = "loans")
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "loan_id")
    private Long loanId; // Primary Key

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "loan_name")
    private String loanName;

    @Column(name = "repayment_type")
    private String repaymentType;

    @Column(name = "interest_rate_type")
    private String interestRateType;

    @Column(name = "interest_rate_min", precision = 5, scale = 2)
    private BigDecimal interestRateMin;

    @Column(name = "interest_rate_max", precision = 5, scale = 2)
    private BigDecimal interestRateMax;

    @Column(name = "interest_rate_avg", precision = 5, scale = 2)
    private BigDecimal interestRateAvg;

    @Column(name = "loan_limit", columnDefinition = "TEXT")
    private String loanLimit;

    @Column(name = "max_loan_limit", precision = 15, scale = 2)
    private BigDecimal maxLoanLimit;

    @Column(name = "max_apartment_price_limit", precision = 15, scale = 2)
    private BigDecimal maxApartmentPriceLimit;

    @Column(name = "loan_additional_cost")
    private String loanAdditionalCost;

    @Column(name = "early_repayment_fee", columnDefinition = "TEXT")
    private String earlyRepaymentFee;

    @Column(name = "overdue_interest_rate")
    private String overdueInterestRate;

    @Column(name = "subscription_method")
    private String subscriptionMethod;

    @Column(name = "max_loan_duration")
    private Integer maxLoanDuration;

    @Column(name = "financial_sector")
    private String financialSector;

    @Column(name = "government_support_type")
    private String governmentSupportType;

    @Column(name = "credit_1_interest", precision = 7, scale = 4)
    private BigDecimal credit1Interest;

    @Column(name = "credit_2_interest", precision = 7, scale = 4)
    private BigDecimal credit2Interest;

    @Column(name = "credit_3_interest", precision = 7, scale = 4)
    private BigDecimal credit3Interest;

    @Column(name = "credit_4_interest", precision = 7, scale = 4)
    private BigDecimal credit4Interest;

    @Column(name = "credit_5_interest", precision = 7, scale = 4)
    private BigDecimal credit5Interest;
}
