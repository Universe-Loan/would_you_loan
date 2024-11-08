package com.woorifisa.wl.model.entity;

import com.woorifisa.wl.model.dto.DocumentDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "documents")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "document_id", nullable = false)
    private Long documentId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "file_path", nullable = false, length = 255)
    private String filePath;

    @Column(name = "file_name", nullable = false, length = 255)
    private String fileName;

    @Column(name = "file_type", nullable = false, length = 50)
    private String fileType;

    @Column(name = "file_size", nullable = false)
    private Long fileSize;

    @Column(name = "uploaded_at", nullable = false)
    private LocalDateTime uploadedAt;

    @Column(name = "status", nullable = false, length = 50)
    private String status;

    // Entity -> DTO로 데이터를 넘기기 위한 메서드
    public DocumentDto toDto() {
        return DocumentDto.builder()
                .documentId(this.documentId)
                .userId(this.userId)
                .filePath(this.filePath)
                .fileName(this.fileName)
                .fileType(this.fileType)
                .fileSize(this.fileSize)
                .uploadedAt(this.uploadedAt)
                .status(this.status)
                .build();
    }
}