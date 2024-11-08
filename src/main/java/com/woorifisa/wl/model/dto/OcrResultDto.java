package com.woorifisa.wl.model.dto;

import com.woorifisa.wl.model.entity.OcrResult;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OcrResultDto {

    private Long ocrResultId;
    private Long documentId;
    private String extractedData;
    private LocalDateTime processedAt;
    private String processingStatus;

    // DTO -> Entity로 데이터를 전송하는 toEntity() 메서드
    public OcrResult toEntity() {
        OcrResult ocrResult = new OcrResult();
        ocrResult.setOcrResultId(this.getOcrResultId());
        ocrResult.setDocumentId(this.getDocumentId());
        ocrResult.setExtractedData(this.getExtractedData());
        ocrResult.setProcessedAt(this.getProcessedAt());
        ocrResult.setProcessingStatus(this.getProcessingStatus());
        return ocrResult;
    }
}