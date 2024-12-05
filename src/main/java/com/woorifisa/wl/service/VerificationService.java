package com.woorifisa.wl.service;

import com.woorifisa.wl.model.entity.VerificationResult;
import com.woorifisa.wl.repository.VerificationResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class VerificationService {

    private final VerificationResultRepository verificationResultRepository;
    private final RestTemplate restTemplate;


    @Value("${loanio.api.url}")
    private String LoanIoApiUrl;


    public VerificationResult saveVerification(VerificationResult verificationResult) {
        return verificationResultRepository.save(verificationResult);
    }

    public VerificationResult approveVerification(Long verificationId, Long userId) {
        VerificationResult result = verificationResultRepository.findById(verificationId)
                .orElseThrow(() -> new IllegalArgumentException("Verification not found"));

        result.setIsApproved(true);

        if ("신규".equals(result.getApplyType())) {
            // 신규 신청에 대한 추가 로직
            if ("국민은행".equals(result.getNewBankName())) {
                LoanAddMyData(result, userId, "kookmin");
            } else if ("우리은행".equals(result.getNewBankName())) {
                LoanAddMyData(result, userId, "woori");
            } else if ("신한은행".equals(result.getNewBankName())) {
                LoanAddMyData(result, userId, "shinhan");
            } else {
                LoanAddMyData(result, userId, "etc");
            }

        } else if ("대환".equals(result.getApplyType())) {
            if ("국민은행".equals(result.getNewBankName())) {
                LoanRemoveMyData(result, userId);
                LoanAddMyData(result, userId, "kookmin");

            } else if ("우리은행".equals(result.getNewBankName())) {
                LoanRemoveMyData(result, userId);
                LoanAddMyData(result, userId, "woori");

            } else if ("신한은행".equals(result.getNewBankName())) {
                LoanRemoveMyData(result, userId);
                LoanAddMyData(result, userId, "shinhan");

            } else {
                LoanRemoveMyData(result, userId);
                LoanAddMyData(result, userId, "etc");

            }
        }

        return verificationResultRepository.save(result);
    }

    public VerificationResult rejectVerification(Long verificationId) {
        VerificationResult result = verificationResultRepository.findById(verificationId)
                .orElseThrow(() -> new IllegalArgumentException("Verification not found"));

        result.setIsApproved(false);
        return verificationResultRepository.save(result);
    }

    // 대출 추가 로직
    private void LoanAddMyData(VerificationResult result, Long userId, String bankCode) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> loanRequest = new HashMap<>();
        loanRequest.put("user_id", userId);
        loanRequest.put("bank_name", result.getNewBankName());
        loanRequest.put("loan_name", result.getNewLoanName());
        loanRequest.put("loan_category", result.getNewLoanType().toLowerCase());
        loanRequest.put("loan_amount", result.getNewAmount());
        loanRequest.put("interest_rate", result.getNewRate());
        loanRequest.put("loan_start_date", result.getNewStartDate());
        loanRequest.put("loan_end_date", result.getNewEndDate());
        loanRequest.put("loan_status", "Active");
        loanRequest.put("created_at", LocalDate.now().format(formatter));

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(loanRequest, headers);

        String url = LoanIoApiUrl + "/add_" + bankCode + "_loan";
        restTemplate.postForObject(url, request, String.class);
    }

    // 대출 삭제 로직
    private void LoanRemoveMyData(VerificationResult result, Long userId) {
        String bankCode = null;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> loanRequest = new HashMap<>();
        loanRequest.put("user_id", userId);
        loanRequest.put("loan_name", result.getPrvLoanName());
        loanRequest.put("loan_amount", result.getPrvAmount());

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(loanRequest, headers);

        if ("국민은행".equals(result.getPrvBankName())) {
            bankCode = "kookmin";
        } else if ("우리은행".equals(result.getPrvBankName())) {
            bankCode = "woori";
        } else if ("신한은행".equals(result.getPrvBankName())) {
            bankCode = "shinhan";
        } else {
            bankCode = "etc";
        }

        String url = LoanIoApiUrl + "/remove_" + bankCode + "_loan";
        restTemplate.postForObject(url, request, String.class);
    }
}