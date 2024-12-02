package com.woorifisa.wl.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class LoanSessionData {
    private String applyType;
    private NewLoanInfo newLoan;
    private PrvLoanInfo prvLoan;

    @Data
    public static class NewLoanInfo {
        private String loanType;
        private String bankName;
        private String loanName;
        private Long amount;
        private BigDecimal rate;
        private String startDate;
        private String endDate;
    }

    @Data
    public static class PrvLoanInfo {
        private String loanType;
        private String bankName;
        private String loanName;
        private Long amount;
        private BigDecimal rate;
        private String startDate;
        private String endDate;
    }
}