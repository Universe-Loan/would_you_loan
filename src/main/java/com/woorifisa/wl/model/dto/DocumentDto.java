package com.woorifisa.wl.model.dto;

import lombok.Data;

@Data
public class DocumentDto {
    private Long userId;         // 사용자 ID
    private String fileName;     // 파일 이름
    private String fileType;     // 파일 타입
    private String fileS3Path;   // S3 경로
}
