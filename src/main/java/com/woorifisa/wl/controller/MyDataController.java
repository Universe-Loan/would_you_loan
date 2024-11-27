package com.woorifisa.wl.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/mydata-api")
public class MyDataController {
    private final RestTemplate restTemplate;

    @Value("${mydataloan.api.url}")
    private String MyDataLoanApiUrl;

    @Value("${mydataaccount.api.url}")
    private String MyDataAccountApiUrl;


    public MyDataController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostMapping("/loan")
    public ResponseEntity<?> loadMyDataLoan(
            @RequestParam("user_id") String userId) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, String> myDataRequestBody = new HashMap<>();
            myDataRequestBody.put("user_id", userId);

            // header와 body을 합쳐서 request 작성
            HttpEntity<Map<String, String>> request = new HttpEntity<>(myDataRequestBody, headers);

            // HTTP Client (e.g., RestTemplate, WebClient)로 POST 요청
            ResponseEntity<List> myDataLoanResponse = restTemplate.postForEntity(MyDataLoanApiUrl, request, List.class);

            List<Map> loans = myDataLoanResponse.getBody();
            long totalMonthlyPayment = 0;

            if (loans != null) {
                for (Map<String, Object> loan : loans) {
                    long monthlyPayment = calculateMonthlyPayment(
                            Long.parseLong(loan.get("loan_amount").toString()),
                            Double.parseDouble(loan.get("interest_rate").toString()),
                            calculateLoanPeriod(
                                    loan.get("loan_start_date").toString(),
                                    loan.get("loan_end_date").toString()
                            )
                    );
                    loan.put("monthly_payment", monthlyPayment);
                    totalMonthlyPayment += monthlyPayment;
                }
            }

            Map<String, Object> response = new HashMap<>();
            response.put("loans", loans);
            response.put("totalMonthlyPayment", totalMonthlyPayment);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error occurred: " + e.getMessage());
        }
    }

    @PostMapping("/account")
    public ResponseEntity<?> loadMyDataAccount(
            @RequestParam("user_id") String userId) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, String> myDataRequestBody = new HashMap<>();
            myDataRequestBody.put("user_id", userId);

            // header와 body을 합쳐서 request 작성
            HttpEntity<Map<String, String>> request = new HttpEntity<>(myDataRequestBody, headers);

            // HTTP Client (e.g., RestTemplate, WebClient)로 POST 요청
            ResponseEntity<List> myDataAccountResponse = restTemplate.postForEntity(MyDataAccountApiUrl, request, List.class);

            return ResponseEntity.ok(myDataAccountResponse.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error occurred: " + e.getMessage());
        }
    }

    // 원리금균등상환 월상환금 계산
    private long calculateMonthlyPayment(long principal, double annualRate, int years) {
        double monthlyRate = (annualRate / 100) / 12;
        int totalPayments = years * 12;

        return Math.round(principal *
                (monthlyRate * Math.pow(1 + monthlyRate, totalPayments)) /
                (Math.pow(1 + monthlyRate, totalPayments) - 1)
        );
    }

    // 대출기간(연) 계산
    private int calculateLoanPeriod(String startDateStr, String endDateStr) {
        LocalDate startDate = LocalDate.parse(startDateStr);
        LocalDate endDate = LocalDate.parse(endDateStr);

        return (int) ChronoUnit.YEARS.between(startDate, endDate);
    }

}
