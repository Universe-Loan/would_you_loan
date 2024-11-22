package com.woorifisa.wl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UrlController {

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/mypage")
    public String mypage() {
        return "mypage";
    }

    @GetMapping("/loan-find")
    public String loanFind() {
        return "loan_find";
    }

    @GetMapping("/loan-personal-info")
    public String loanPersonalInfo() {
        return "loan_personal_info";
    }

    @GetMapping("/loan-list")
    public String loanList() {
        return "loan_list";
    }

    // 대출 상품 개별 ID 대로 detail?id= 로 이동해야 함
    @GetMapping("/loan-details-govt")
    public String loanDetailsGovt() {
        return "loan_details_govt";
    }

    @GetMapping("/loan-details-bank")
    public String loanDetailsBank() {
        return "loan_details_bank";
    }

//    @GetMapping("/loan-details/{id}")
//    public String loanDetails(@PathVariable Long id, Model model) {
//        // 여기에서 id를 사용하여 대출 상품 정보를 조회하고 모델에 추가
//        // 예: Loan loan = loanService.getLoanById(id);
//        // model.addAttribute("loan", loan);
//        return "loan_details";
//    }

    @GetMapping("/loan-ocr")
    public String loanOCR() {
        return "loan_ocr";
    }

    @GetMapping("/change-find")
    public String changeFind() {
        return "change_find";
    }

// AptInfoController에서 연결
//    @GetMapping("/apt-find")
//    public String aptFind() {
//        return "apt_find";
//    }
//
//    @GetMapping("/apt-report")
//    public String aptReport() {
//        return "apt_report";
//    }

//    @GetMapping("/market-analysis")
//    public String marketAnalysis() {
//        return "market_analysis";
//    }

    @GetMapping("/my-loans")
    public String myLoans() {
        return "my_loans";
    }

}

