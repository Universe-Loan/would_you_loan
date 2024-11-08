package com.woorifisa.wl.model.entity;

import com.woorifisa.wl.model.dto.OcrResultDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "ocr_results")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OcrResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ocr_result_id", nullable = false)
    private Long ocrResultId;

    @Column(name = "document_id", nullable = false)
    private Long documentId;

    @Column(name = "extracted_data", nullable = false, columnDefinition = "JSON")
    private String extractedData;

    @Column(name = "processed_at")
    private LocalDateTime processedAt;

    @Column(name = "processing_status", nullable = false, length = 50)
    private String processingStatus;

    // Entity -> DTO로 데이터를 넘기기 위한 메서드
    public OcrResultDto toDto() {
        return OcrResultDto.builder()
                .ocrResultId(this.ocrResultId)
                .documentId(this.documentId)
                .extractedData(this.extractedData)
                .processedAt(this.processedAt)
                .processingStatus(this.processingStatus)
                .build();
    }
}