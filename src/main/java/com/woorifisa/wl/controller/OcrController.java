package com.woorifisa.wl.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.net.URI;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/ocr-api")
public class OcrController {
    private final S3Client s3Client;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Value("${ocr.api.url}")
    private String ocrApiUrl;

    public OcrController(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    @PostMapping("/upload-result")
    public ResponseEntity<?> uploadFileAndProcessOCR(
            @RequestParam("file") MultipartFile file,
            @RequestParam("user_id") String userId) {
        try {
            // 1. 파일 업로드
            String fileName = Paths.get(file.getOriginalFilename()).getFileName().toString();
            String baseName = fileName.contains(".")
                    ? fileName.substring(0, fileName.lastIndexOf('.'))
                    : fileName;
            String s3Key = userId + "_" + fileName;
            s3Client.putObject(PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(s3Key)
                            .build(),
                    software.amazon.awssdk.core.sync.RequestBody.fromBytes(file.getBytes()));

            // 2. OCR API 호출
            Map<String, String> ocrRequestBody = new HashMap<>();
            ocrRequestBody.put("user_id", userId);
            ocrRequestBody.put("file_name", baseName);

            // HTTP Client (e.g., RestTemplate, WebClient)로 POST 요청
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Map> ocrResponse = restTemplate.postForEntity(ocrApiUrl, ocrRequestBody, Map.class);

            // 3. OCR 결과 반환
            return ResponseEntity.ok(ocrResponse.getBody());

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error occurred: " + e.getMessage());
        }
    }
}
