package com.woorifisa.wl.controller;

import com.woorifisa.wl.model.dto.DocumentDto;
import com.woorifisa.wl.service.DocumentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Paths;

@RestController
@RequestMapping("/ocr-document")
public class DocumentController {

    private final DocumentService documentService;

    @Value("${aws.s3.endpoint}")
    private String endpoint;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping("/record-doc-info")
    public ResponseEntity<String> uploadDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam("user_id") Long userId,
            HttpSession session) {
        try {
            String fileName = Paths.get(file.getOriginalFilename()).getFileName().toString();
            String baseName = fileName.contains(".")
                    ? fileName.substring(0, fileName.lastIndexOf('.'))
                    : fileName;
            String fileType = fileName.substring(fileName.lastIndexOf('.') + 1); // 확장자 추출
            // S3 경로 생성
            String fileS3Path = endpoint + "ocr/" + userId + "_" + fileName;

            // DB에 정보 저장
            DocumentDto documentDto = new DocumentDto();
            documentDto.setUserId(userId);
            documentDto.setFileName(baseName);
            documentDto.setFileType(fileType);
            documentDto.setFileS3Path(fileS3Path);

            Long documentId = documentService.saveDocument(documentDto);

            // 세션에 저장
            session.setAttribute("documentId", documentId);

            return ResponseEntity.ok("파일 업로드 중입니다. ID: " + documentId);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error occurred: " + e.getMessage());
        }
    }
}
