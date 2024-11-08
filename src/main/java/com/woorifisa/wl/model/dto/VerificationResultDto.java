package com.woorifisa.wl.model.dto;

import com.woorifisa.wl.model.entity.VerificationResult;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerificationResultDto {

    private Long verificationId;
    private Long ocrResultId;
    private String isApproved;
    private String verificationStatus;
    private String reviewerComments;
    private LocalDateTime verifiedAt;
    private Long verifiedBy;

    // DTO -> Entity로 데이터를 전송하는 toEntity() 메서드
    public VerificationResult toEntity() {
        VerificationResult verificationResult = new VerificationResult();
        verificationResult.setVerificationId(this.getVerificationId());
        verificationResult.setOcrResultId(this.getOcrResultId());
        verificationResult.setIsApproved(this.getIsApproved());
        verificationResult.setVerificationStatus(this.getVerificationStatus());
        verificationResult.setReviewerComments(this.getReviewerComments());
        verificationResult.setVerifiedAt(this.getVerifiedAt());
        verificationResult.setVerifiedBy(this.getVerifiedBy());
        return verificationResult;
    }
}