package com.woorifisa.wl.model.dto;

import lombok.Data;

@Data
public class NewsArticleDto {
    private String articleId;
    private String articleDate;
    private String company;
    private String title;
    private String keywords;
    private String content;
    private String url;
    private String sentiment;
}
