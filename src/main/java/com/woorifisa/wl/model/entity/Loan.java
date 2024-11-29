package com.woorifisa.wl.model.entity;

import jakarta.persistence.*;
import lombok.Data;

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

    @Column(name = "interest_rate_min")
    private String interestRateMin;

    @Column(name = "interest_rate_max")
    private String interestRateMax;

    @Column(name = "interest_rate_avg")
    private String interestRateAvg;

    @Column(name = "loan_limit")
    private String loanLimit;

    @Column(name = "max_loan_limit")
    private String maxLoanLimit;

    @Column(name = "max_apartment_price_limit")
    private String maxApartmentPriceLimit;

    @Column(name = "loan_additional_cost")
    private String loanAdditionalCost;

    @Column(name = "early_repayment_fee")
    private String earlyRepaymentFee;

    @Column(name = "overdue_interest_rate")
    private String overdueInterestRate;

    @Column(name = "subscription_method")
    private String subscriptionMethod;

    @Column(name = "max_loan_duration")
    private String maxLoanDuration;

    @Column(name = "financial_sector")
    private String financialSector;

    @Column(name = "government_support_type")
    private String governmentSupportType;

    @Column(name = "credit_1_interest")
    private String credit1Interest;

    @Column(name = "credit_2_interest")
    private String credit2Interest;

    @Column(name = "credit_3_interest")
    private String credit3Interest;

    @Column(name = "credit_4_interest")
    private String credit4Interest;

    @Column(name = "credit_5_interest")
    private String credit5Interest;
}
