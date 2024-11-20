package com.woorifisa.wl.repository;

import com.woorifisa.wl.model.entity.NewsArticle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsArticleRepository extends JpaRepository<NewsArticle, String> {
    Page<NewsArticle> findAll(Pageable pageable);
}
