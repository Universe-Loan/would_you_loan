package com.woorifisa.wl.service;

import com.woorifisa.wl.repository.LoanSearchRecordRepository;
import org.springframework.stereotype.Service;

@Service
public class LoanSearchRecordService {
    private final LoanSearchRecordRepository loanSearchRecordRepository;

    public LoanSearchRecordService(LoanSearchRecordRepository loanSearchRecordRepository) {
        this.loanSearchRecordRepository = loanSearchRecordRepository;
    }
}
