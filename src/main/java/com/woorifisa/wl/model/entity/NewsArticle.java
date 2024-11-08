package com.woorifisa.wl.model.entity;

import com.woorifisa.wl.model.dto.NewsArticleDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "news_articles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewsArticle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_id", nullable = false)
    private Long articleId;

    @Column(name = "article_date")
    private LocalDateTime articleDate;

    @Column(name = "press", length = 255)
    private String press;

    @Column(name = "author", length = 255)
    private String author;

    @Column(name = "title", length = 500)
    private String title;

    @Column(name = "category1", length = 100)
    private String category1;

    @Column(name = "category2", length = 100)
    private String category2;

    @Column(name = "category3", length = 100)
    private String category3;

    @Column(name = "keywords", length = 500)
    private String keywords;

    @Column(name = "url", length = 1000)
    private String url;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Entity -> DTO로 데이터를 넘기기 위한 메서드
    public NewsArticleDto toDto() {
        return NewsArticleDto.builder()
                .articleId(this.articleId)
                .articleDate(this.articleDate)
                .press(this.press)
                .author(this.author)
                .title(this.title)
                .category1(this.category1)
                .category2(this.category2)
                .category3(this.category3)
                .keywords(this.keywords)
                .url(this.url)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .build();
    }
}