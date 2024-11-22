package com.woorifisa.wl.model.dto;

import lombok.Data;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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

    // 키워드 문자열을 List로 변환하는 메서드
    public List<String> getKeywordsList() {
        if (keywords == null || keywords.isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.asList(keywords.split(","));
    }
}
