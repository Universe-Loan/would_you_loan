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

            return ResponseEntity.ok(myDataLoanResponse.getBody());
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
}
