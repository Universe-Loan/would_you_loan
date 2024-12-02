package com.woorifisa.wl.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
@Table(name = "verifcation_results")
public class VerificationResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "verifcation_id")
    private Long verificationId;

    @Column(name = "ocr_result_id")
    private Long ocrResultId;

    @Column(name = "upload_at")
    private String uploadAt;

    @Column(name = "is_approved")
    private Boolean isApproved;


    // 신청 대출 정보
    @Column(name = "apply_type")
    private String applyType;

    // 신규 대출 정보
    @Column(name = "new_loan_type")
    private String newLoanType;

    @Column(name = "new_bank_name")
    private String newBankName;

    @Column(name = "new_loan_name")
    private String newLoanName;

    @Column(name = "new_amount")
    private Long newAmount;

    @Column(name = "new_rate", precision = 7, scale = 4)
    private BigDecimal newRate;

    @Column(name = "new_start_date")
    private String newStartDate;

    @Column(name = "new_end_date")
    private String newEndDate;


    // 신규 대출 정보
    @Column(name = "prv_loan_type")
    private String prvLoanType;

    @Column(name = "prv_bank_name")
    private String prvBankName;

    @Column(name = "prv_loan_name")
    private String prvLoanName;

    @Column(name = "prv_amount")
    private Long prvAmount;

    @Column(name = "prv_rate", precision = 7, scale = 4)
    private BigDecimal prvRate;

    @Column(name = "prv_start_date")
    private String prvStartDate;

    @Column(name = "prv_end_date")
    private String prvEndDate;

}
