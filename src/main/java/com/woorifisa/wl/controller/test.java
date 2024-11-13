package com.woorifisa.wl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class test {

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

//    @GetMapping("/loan-details/{id}")
//    public String loanDetails(@PathVariable Long id, Model model) {
//        // 여기에서 id를 사용하여 대출 상품 정보를 조회하고 모델에 추가
//        // 예: Loan loan = loanService.getLoanById(id);
//        // model.addAttribute("loan", loan);
//        return "loan_details";
//    }
}
