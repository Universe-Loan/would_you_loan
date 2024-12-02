package com.woorifisa.wl.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiKeyController {
    @Value("${API_KEY_DATA_EH}")
    private String apiKey;

    @GetMapping("/api/key/DATA")
    public String getApiKey() {
        return apiKey;
    }
}