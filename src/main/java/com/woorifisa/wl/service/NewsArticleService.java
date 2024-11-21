package com.woorifisa.wl.service;

import com.woorifisa.wl.model.dto.NewsArticleDto;
import com.woorifisa.wl.model.entity.NewsArticle;
import com.woorifisa.wl.repository.NewsArticleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NewsArticleService {

    private final NewsArticleRepository newsArticleRepository;

    public NewsArticleService(NewsArticleRepository newsArticleRepository) {
        this.newsArticleRepository = newsArticleRepository;
    }

    public Page<NewsArticleDto> getPaginatedArticles(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return newsArticleRepository.findAll(pageable).map(this::convertToDto);
    }

    // 엔티티를 DTO로 변환
    public List<NewsArticleDto> getAllNewsArticles() {
        List<NewsArticle> articles = newsArticleRepository.findAll();
        return articles.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    private NewsArticleDto convertToDto(NewsArticle article) {
        NewsArticleDto dto = new NewsArticleDto();
        dto.setArticleId(article.getArticleId());
        dto.setArticleDate(article.getArticleDate().toString());
        dto.setCompany(article.getCompany());
        dto.setTitle(article.getTitle());
        dto.setKeywords(article.getKeywords());
        dto.setContent(article.getContent());
        dto.setUrl(article.getUrl());
        dto.setSentiment(article.getSentiment());
        return dto;
    }
}
