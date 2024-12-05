package com.woorifisa.wl.controller;

import com.woorifisa.wl.model.entity.VerificationResult;
import com.woorifisa.wl.repository.VerificationResultRepository;
import com.woorifisa.wl.service.VerificationService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class VerificationController {

    private final VerificationResultRepository verificationResultRepository;
    private final VerificationService verificationService;

    @PostMapping("/verification/save")
    @ResponseBody
    public ResponseEntity<String> saveVerificationResult(@RequestBody VerificationResult verificationResult) {
        try {
            verificationResultRepository.save(verificationResult);
            return ResponseEntity.ok("대출 신청이 완료 되었습니다. 영업일 기준 3~4일 소요 예정입니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("저장 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    @PostMapping("/verification/{verificationId}/approve")
    @ResponseBody
    public ResponseEntity<String> approveVerification(
            @PathVariable Long verificationId,
            @RequestParam String userId) {
        try {
            Long userIdLong = Long.parseLong(userId);
            verificationService.approveVerification(verificationId, userIdLong);
            return ResponseEntity.ok("승인 처리되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("승인 처리 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    @PostMapping("/verification/{verificationId}/reject")
    @ResponseBody
    public ResponseEntity<String> rejectVerification(@PathVariable Long verificationId) {
        try {
            verificationService.rejectVerification(verificationId);
            return ResponseEntity.ok("거절 처리되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("거절 처리 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
}
