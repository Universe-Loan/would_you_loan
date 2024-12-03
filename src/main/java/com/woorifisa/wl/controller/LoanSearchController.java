package com.woorifisa.wl.controller;

import com.woorifisa.wl.model.dto.LoanSearchDto;
import com.woorifisa.wl.model.entity.Loan;
import com.woorifisa.wl.repository.LoanRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class LoanSearchController {

    private final LoanRepository loanRepository;

    // 대출 검색 처리
    @PostMapping("/loans/search")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> searchLoans(@RequestBody LoanSearchDto searchDto,
                                                           HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        try {
            // DTO에서 신용등급 가져오기
            Integer creditRating = searchDto.getCreditRating();
            if (creditRating == null) {
                throw new RuntimeException("신용등급 정보가 없습니다.");
            }

            // 검색 조건 변환
            BigDecimal requestAmount = new BigDecimal(searchDto.getLoanAmount().replace(",", ""));
            BigDecimal apartmentPrice = new BigDecimal(searchDto.getApartmentPrice().replace(",", ""));
            Integer loanTerm = Integer.parseInt(searchDto.getLoanTerm());

            // 대출 상품 검색
            List<Loan> eligibleLoans = loanRepository.findEligibleLoans(
                    creditRating,
                    requestAmount,
                    loanTerm,
                    searchDto.getLoanType(),
                    apartmentPrice
            );

            // 검색 조건과 결과를 세션에 저장
            session.setAttribute("loanSearchData", searchDto);
            session.setAttribute("eligibleLoans", eligibleLoans);

//            System.out.println(eligibleLoans);

            response.put("success", true);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


}
