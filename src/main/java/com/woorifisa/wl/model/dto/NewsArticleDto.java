package com.woorifisa.wl.model.dto;

import com.woorifisa.wl.model.entity.NewsArticle;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewsArticleDto {

    private Long articleId;
    private LocalDateTime articleDate;
    private String press;
    private String author;
    private String title;
    private String category1;
    private String category2;
    private String category3;
    private String keywords;
    private String url;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // DTO -> Entity로 데이터를 전송하는 toEntity() 메서드
    public NewsArticle toEntity() {
        NewsArticle newsArticle = new NewsArticle();
        newsArticle.setArticleId(this.getArticleId());
        newsArticle.setArticleDate(this.getArticleDate());
        newsArticle.setPress(this.getPress());
        newsArticle.setAuthor(this.getAuthor());
        newsArticle.setTitle(this.getTitle());
        newsArticle.setCategory1(this.getCategory1());
        newsArticle.setCategory2(this.getCategory2());
        newsArticle.setCategory3(this.getCategory3());
        newsArticle.setKeywords(this.getKeywords());
        newsArticle.setUrl(this.getUrl());
        newsArticle.setCreatedAt(this.getCreatedAt());
        newsArticle.setUpdatedAt(this.getUpdatedAt());
        return newsArticle;
    }
}