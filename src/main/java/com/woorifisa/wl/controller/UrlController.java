package com.woorifisa.wl.controller;

import com.woorifisa.wl.model.dto.LoanSearchDto;
import com.woorifisa.wl.model.dto.LoanSessionData;
import com.woorifisa.wl.model.entity.Loan;
import com.woorifisa.wl.model.entity.VerificationResult;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    // 대출 목록 페이지
    @GetMapping("/loan-list")
    public String showLoanList(Model model, HttpSession session) {
        // 세션에서 검색 데이터와 결과 가져오기
        LoanSearchDto searchData = (LoanSearchDto) session.getAttribute("loanSearchData");
        List<Loan> eligibleLoans = (List<Loan>) session.getAttribute("eligibleLoans");

        // 검색 조건 전달
        if (searchData != null) {
            model.addAttribute("searchData", searchData);
        }

        // 검색 결과 전달
        if (eligibleLoans != null && !eligibleLoans.isEmpty()) {
            model.addAttribute("loans", eligibleLoans);
        } else {
            model.addAttribute("noResults", true);
        }

        return "loan_list";  // loan_list.html
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
    public String loanOCR(Model model, HttpSession session) {
        LoanSessionData loanData = (LoanSessionData) session.getAttribute("loanData");
        if (loanData != null) {
            model.addAttribute("sessionLoanData", loanData);
        }
        return "loan_ocr";
    }

    @PostMapping("/save-loan-session")
    @ResponseBody
    public Map<String, Boolean> saveLoanData(@RequestBody LoanSessionData loanData,
                                             HttpSession session) {
        session.setAttribute("loanData", loanData);
        Map<String, Boolean> response = new HashMap<>();
        response.put("success", true);
        return response;
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
//
//    @GetMapping("/apt-list")
//    public String aptList() {
//        return "apt_list";
//    }

//    MarketAnalysisController에서 연결
//    @GetMapping("/market-analysis")
//    public String marketAnalysis() {
//        return "market_analysis";
//    }

    @GetMapping("/my-loans")
    public String myLoans() {
        return "my_loans";
    }

    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }

    @GetMapping("/login-extra")
    public String loginExtra() {
        return "login_extra";
    }

}

