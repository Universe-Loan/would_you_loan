package com.woorifisa.wl.controller;

import com.woorifisa.wl.model.dto.NewsArticleDto;
import com.woorifisa.wl.service.NewsArticleService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/market-analysis")
public class MarketAnalysisController {
    private final NewsArticleService newsArticleService;

    public MarketAnalysisController(NewsArticleService newsArticleService) {
        this.newsArticleService = newsArticleService;
    }

    @GetMapping
    public String getArticles(@RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size,
                              Model model) {
        Page<NewsArticleDto> articlesPage = newsArticleService.getPaginatedArticles(page, size);
        int totalPages = articlesPage.getTotalPages();
        int currentPage = page;

        // 페이지 번호 범위 계산 (7개씩 표시)
        int pageRange = 7; // 보여줄 페이지 번호 개수
        int startPage = Math.max(0, currentPage - pageRange / 2);
        int endPage = Math.min(totalPages - 1, startPage + pageRange - 1);

        // 시작 페이지 보정
        if ((endPage - startPage) < (pageRange - 1)) {
            startPage = Math.max(0, endPage - pageRange + 1);
        }

        model.addAttribute("articles", articlesPage.getContent()); // 현재 페이지 데이터
        model.addAttribute("currentPage", currentPage); // 현재 페이지
        model.addAttribute("totalPages", totalPages); // 총 페이지 수
        model.addAttribute("startPage", startPage); // 시작 페이지 번호
        model.addAttribute("endPage", endPage); // 끝 페이지 번호

        return "market_analysis"; // 렌더링할 템플릿 이름
    }
}
