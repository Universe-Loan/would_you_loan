package com.woorifisa.wl.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/rag")
public class RagController {
    @Value("${rag.api.url}")
    private String ragApiUrl;

    private final RestTemplate restTemplate;

    public RagController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostMapping("/chat")
    public ResponseEntity<?> getChatResponse(@RequestBody Map<String, Object> request) {
        try {
            // 요청 데이터 준비
            Map<String, Object> payload = new HashMap<>();
            payload.put("user_id", request.get("user_id"));
            payload.put("user_question", request.get("user_question"));

            // 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");

            // API 요청
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);
            ResponseEntity<Map> response = restTemplate.postForEntity(ragApiUrl, entity, Map.class);

            // 응답 반환
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            // 오류 발생 시 처리
            return ResponseEntity.status(500).body("오류가 발생했습니다: " + e.getMessage());
        }
    }
}
