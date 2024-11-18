package com.woorifisa.wl.controller;

import com.woorifisa.wl.model.dto.OcrResultDto;
import com.woorifisa.wl.service.OcrResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/record-ocr")
public class OcrResultController {

    private final OcrResultService ocrResultService;

    @Autowired
    public OcrResultController(OcrResultService ocrResultService) {
        this.ocrResultService = ocrResultService;
    }

    @PostMapping("/add-ocr-result")
    public ResponseEntity<String> addOcrResult(@RequestBody OcrResultDto ocrResultDto) {
        ocrResultService.addOcrResult(ocrResultDto);
        return ResponseEntity.ok("OCR 결과가 저장되었습니다.");
    }

    @PostMapping("/update-ocr-result")
    public ResponseEntity<String> updateOcrResult(@RequestBody OcrResultDto ocrResultDto) {
        ocrResultService.updateOcrResult(ocrResultDto);
        return ResponseEntity.ok("OCR 결과가 업데이트 되었습니다.");
    }
}
