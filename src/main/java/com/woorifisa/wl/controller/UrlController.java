package com.woorifisa.wl.controller;

import com.woorifisa.wl.model.dto.LoanSearchDto;
import com.woorifisa.wl.model.dto.LoanSessionData;
import com.woorifisa.wl.model.entity.*;
import com.woorifisa.wl.repository.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class UrlController {
    private final UserRepository userRepository;
    private final LoanRepository loanRepository;
    private final VerificationResultRepository verificationResultRepository;
    private final OcrResultRepository ocrResultRepository;
    private final DocumentRepository documentRepository;

    public UrlController(UserRepository userRepository, LoanRepository loanRepository, VerificationResultRepository verificationResultRepository, OcrResultRepository ocrResultRepository, DocumentRepository documentRepository) {
        this.userRepository = userRepository;
        this.loanRepository = loanRepository;
        this.verificationResultRepository = verificationResultRepository;
        this.ocrResultRepository = ocrResultRepository;
        this.documentRepository = documentRepository;
    }

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

        // 세션의 user_id로 유저 정보 조회
        Long userId = (Long) session.getAttribute("user_id");
        if (userId != null) {
            User user = userRepository.findById(userId)
                    .orElse(null);
            if (user != null) {
                model.addAttribute("houseNum", user.getHouseNum());
            }
        }


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
    @PostMapping("/loan-details-govt")
    public String showGovernmentLoanDetails(@RequestParam Long loanId,
                                            @RequestParam BigDecimal loanAmount,
                                            @RequestParam Integer loanTerm,
                                            Model model) {
        // ... 정부지원 대출 상세 페이지 로직
        // 대출 정보 조회
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        // 모델에 데이터 추가
        model.addAttribute("loan", loan);
        model.addAttribute("loanAmount", loanAmount);
        model.addAttribute("loanTerm", loanTerm);

        return "loan_details_govt";
    }

    @PostMapping("/loan-details-bank")
    public String showBankLoanDetails(@RequestParam Long loanId,
                                      @RequestParam BigDecimal loanAmount,
                                      @RequestParam Integer loanTerm,
                                      Model model) {
        // ... 은행 대출 상세 페이지 로직
        // 대출 정보 조회
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        // 모델에 데이터 추가
        model.addAttribute("loan", loan);
        model.addAttribute("loanAmount", loanAmount);
        model.addAttribute("loanTerm", loanTerm);

        return "loan_details_bank";
    }


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
    public String admin(Model model) {
        List<VerificationResult> verifications = verificationResultRepository.findAll();

        // 각 검증 결과에 대한 추가 정보 조회
        List<Map<String, Object>> verificationDetails = verifications.stream()
                .sorted(Comparator.comparing(VerificationResult::getUploadAt).reversed())
                .map(verification -> {
                    Map<String, Object> detail = new HashMap<>();
                    detail.put("verification", verification);

                    // OCR 결과 조회
                    OcrResult ocrResult = ocrResultRepository.findById(verification.getOcrResultId())
                            .orElse(null);
                    detail.put("ocrResult", ocrResult);

                    // 문서 정보 조회
                    if (ocrResult != null) {
                        Document document = documentRepository.findById(ocrResult.getDocumentId())
                                .orElse(null);
                        detail.put("document", document);

                        if (document != null) {
                            detail.put("userId", document.getUserId());
                        }
                    }

                    return detail;
                })
                .collect(Collectors.toList());

        model.addAttribute("verificationDetails", verificationDetails);

        return "admin";
    }

    @GetMapping("/login-extra")
    public String loginExtra() {
        return "login_extra";
    }

}

