package com.woorifisa.wl.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/credit-score")
public class CreditScoreController {
    private final RestTemplate restTemplate;

    @Value("${creditscore.api.url}")
    private String creditScoreApiUrl;

    public CreditScoreController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getCreditScore(@PathVariable String userId) {
        try {
            // FastAPI 서버 호출
            ResponseEntity<List> response = restTemplate.getForEntity(
                    creditScoreApiUrl + "/" + userId,
                    List.class
            );

            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error occurred: " + e.getMessage());
        }
    }
}
