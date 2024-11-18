package com.woorifisa.wl.service;

import com.woorifisa.wl.model.dto.DocumentDto;
import com.woorifisa.wl.model.entity.Document;
import com.woorifisa.wl.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DocumentService {

    private final DocumentRepository documentRepository;

    @Autowired
    public DocumentService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    public void saveDocument(DocumentDto documentDto) {
        Document document = new Document();
        document.setUserId(documentDto.getUserId());
        document.setFileName(documentDto.getFileName());
        document.setFileType(documentDto.getFileType());
        document.setFileS3Path(documentDto.getFileS3Path());
        document.setUploadedAt(LocalDateTime.now());

        documentRepository.save(document);
    }
}
