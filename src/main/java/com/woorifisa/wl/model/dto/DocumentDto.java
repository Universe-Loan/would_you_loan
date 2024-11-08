package com.woorifisa.wl.model.dto;

import com.woorifisa.wl.model.entity.Document;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentDto {

    private Long documentId;
    private Long userId;
    private String filePath;
    private String fileName;
    private String fileType;
    private Long fileSize;
    private LocalDateTime uploadedAt;
    private String status;

    // DTO -> Entity로 데이터를 전송하는 toEntity() 메서드
    public Document toEntity() {
        Document document = new Document();
        document.setDocumentId(this.getDocumentId());
        document.setUserId(this.getUserId());
        document.setFilePath(this.getFilePath());
        document.setFileName(this.getFileName());
        document.setFileType(this.getFileType());
        document.setFileSize(this.getFileSize());
        document.setUploadedAt(this.getUploadedAt());
        document.setStatus(this.getStatus());
        return document;
    }
}