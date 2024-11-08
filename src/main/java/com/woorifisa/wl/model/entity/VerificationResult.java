package com.woorifisa.wl.model.entity;

import com.woorifisa.wl.model.dto.VerificationResultDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "verification_results")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerificationResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "verification_id", nullable = false)
    private Long verificationId;

    @Column(name = "ocr_result_id", nullable = false)
    private Long ocrResultId;

    @Column(name = "is_approved", nullable = false, length = 5)
    private String isApproved;

    @Column(name = "verification_status", nullable = false, length = 50)
    private String verificationStatus;

    @Column(name = "reviewer_comments", length = 500)
    private String reviewerComments;

    @Column(name = "verified_at")
    private LocalDateTime verifiedAt;

    @Column(name = "verified_by")
    private Long verifiedBy;

    // Entity -> DTO로 데이터를 넘기기 위한 메서드
    public VerificationResultDto toDto() {
        return VerificationResultDto.builder()
                .verificationId(this.verificationId)
                .ocrResultId(this.ocrResultId)
                .isApproved(this.isApproved)
                .verificationStatus(this.verificationStatus)
                .reviewerComments(this.reviewerComments)
                .verifiedAt(this.verifiedAt)
                .verifiedBy(this.verifiedBy)
                .build();
    }
}