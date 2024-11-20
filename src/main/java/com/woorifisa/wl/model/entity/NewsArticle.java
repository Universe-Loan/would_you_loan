package com.woorifisa.wl.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "news_articles")
public class NewsArticle {

    @Id
    @Column(name = "article_id")
    private String articleId;

    @Column(name = "article_date")
    private LocalDate articleDate;

    @Column(name = "company")
    private String company;

    @Column(name = "title")
    private String title;

    @Column(name = "keywords")
    private String keywords;

    @Column(name = "content")
    private String content;

    @Column(name = "url")
    private String url;
}
