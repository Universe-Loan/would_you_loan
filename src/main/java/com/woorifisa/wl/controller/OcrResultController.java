package com.woorifisa.wl.controller;

import com.woorifisa.wl.model.dto.OcrResultDto;
import com.woorifisa.wl.service.OcrResultService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/record-ocr")
public class OcrResultController {

    private final OcrResultService ocrResultService;

    @Autowired
    public OcrResultController(OcrResultService ocrResultService) {
        this.ocrResultService = ocrResultService;
    }

    @PostMapping("/add-ocr-result")
    public ResponseEntity<Map<String, Object>> addOcrResult(@RequestBody OcrResultDto ocrResultDto, HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        try {
            // 서비스 호출하여 결과 저장 및 ID 반환
            Long documentId = (Long) session.getAttribute("documentId");
            Long ocrResultId = ocrResultService.addOcrResult(ocrResultDto, documentId);

            // 세션에 저장
            session.setAttribute("ocrResultId", ocrResultId);

            // 응답 데이터 설정
            response.put("success", true);
            response.put("message", "OCR 결과가 저장되었습니다.");
            response.put("ocrResultId", ocrResultId);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "저장 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/update-ocr-result")
    public ResponseEntity<String> updateOcrResult(@RequestBody OcrResultDto ocrResultDto) {
        ocrResultService.updateOcrResult(ocrResultDto);
        return ResponseEntity.ok("OCR 결과가 업데이트 되었습니다.");
    }



}
