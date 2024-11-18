package com.woorifisa.wl.repository;

import com.woorifisa.wl.model.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document, Long> {
}