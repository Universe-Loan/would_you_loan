package com.woorifisa.wl.controller;

import com.woorifisa.wl.model.entity.AssetRecord;
import com.woorifisa.wl.repository.AssetRecordRepository;
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
    private final AssetRecordRepository assetRecordRepository;

    @Value("${mydataloan.api.url}")
    private String MyDataLoanApiUrl;

    @Value("${mydataaccount.api.url}")
    private String MyDataAccountApiUrl;


    public MyDataController(RestTemplate restTemplate, AssetRecordRepository assetRecordRepository) {
        this.restTemplate = restTemplate;
        this.assetRecordRepository = assetRecordRepository;
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
            // 1. MyData API 호출하여 현재 계좌 정보 조회
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, String> myDataRequestBody = new HashMap<>();
            myDataRequestBody.put("user_id", userId);

            // header와 body을 합쳐서 request 작성
            HttpEntity<Map<String, String>> request = new HttpEntity<>(myDataRequestBody, headers);

            // HTTP Client (e.g., RestTemplate, WebClient)로 POST 요청
            ResponseEntity<List> myDataAccountResponse = restTemplate.postForEntity(MyDataAccountApiUrl, request, List.class);
            List<Map> accounts = myDataAccountResponse.getBody();

            // 2. 현재 계좌 잔액 총합 계산
            long currentTotalBalance = 0;
            for (Map<String, Object> account : accounts) {
                currentTotalBalance += Long.parseLong(account.get("balance").toString());
            }

            // 3. 지난달 자산 기록 조회
            LocalDate now = LocalDate.now();
            LocalDate lastMonth = now.minusMonths(1);

            AssetRecord lastMonthRecord = assetRecordRepository.findByUserIdAndYearAndMonthAndAssetType(
                    Long.parseLong(userId),
                    lastMonth.getYear(),
                    lastMonth.getMonthValue(),
                    "ASSET"
            );

            // 4. 변동률 계산
            double changeRate = 0.0;
            if (lastMonthRecord != null) {
                long lastMonthAmount = lastMonthRecord.getAmount();
                changeRate = ((double)(currentTotalBalance - lastMonthAmount) / lastMonthAmount) * 100;
            }

            // 5. 응답 데이터 구성
            Map<String, Object> response = new HashMap<>();
            response.put("accounts", accounts);
            response.put("currentTotal", currentTotalBalance);
            response.put("lastMonthAmount", lastMonthRecord != null ? lastMonthRecord.getAmount() : 0);
            response.put("changeRate", changeRate);
            response.put("changeAmount", lastMonthRecord != null ?
                    currentTotalBalance - lastMonthRecord.getAmount() : 0);

            return ResponseEntity.ok(response);
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
