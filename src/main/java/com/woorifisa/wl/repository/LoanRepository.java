package com.woorifisa.wl.repository;

import com.woorifisa.wl.model.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    // 기본 CRUD 메서드는 JpaRepository에서 제공됨

    // 모든 필터링 조건을 적용한 대출 상품 검색
    @Query("SELECT l FROM Loan l WHERE " +
            // 1. 신용등급에 따른 금리가 존재하는지
            "CASE " +
            "   WHEN :creditRating = 1 THEN l.credit1Interest IS NOT NULL " +
            "   WHEN :creditRating = 2 THEN l.credit2Interest IS NOT NULL " +
            "   WHEN :creditRating = 3 THEN l.credit3Interest IS NOT NULL " +
            "   WHEN :creditRating = 4 THEN l.credit4Interest IS NOT NULL " +
            "   WHEN :creditRating = 5 THEN l.credit5Interest IS NOT NULL " +
            "END AND " +
            // 2. 대출 한도 체크
            "l.maxLoanLimit >= :requestAmount AND " +
            // 3. 대출 기간 체크
            "l.maxLoanDuration >= :loanTerm AND " +
            // 4. 대출 대상 유형 체크 (유형이 선택된 경우에만)
            "(:loanType IS NULL OR l.governmentSupportType LIKE %:loanType%) AND " +
            // 5. LTV 체크 (아파트 가격 * LTV비율 >= 대출 신청금액)
            ":requestAmount <= :apartmentPrice * (l.maxLoanLimit / :apartmentPrice)")
    List<Loan> findEligibleLoans(
            @Param("creditRating") Integer creditRating,
            @Param("requestAmount") BigDecimal requestAmount,
            @Param("loanTerm") Integer loanTerm,
            @Param("loanType") String loanType,
            @Param("apartmentPrice") BigDecimal apartmentPrice
    );

    // 기본 조회 메서드들
    List<Loan> findByBankName(String bankName);
    List<Loan> findByFinancialSector(String financialSector);
    List<Loan> findByMaxLoanDurationGreaterThanEqual(Integer duration);
    List<Loan> findByMaxLoanLimitGreaterThanEqual(BigDecimal amount);

    // 특정 은행의 특정 대출 상품 조회
    Optional<Loan> findByBankNameAndLoanName(String bankName, String loanName);

    // 정부지원 대출 상품 조회
    List<Loan> findByGovernmentSupportTypeContaining(String supportType);

    // 특정 금리 범위 내의 대출 상품 조회
    @Query("SELECT l FROM Loan l WHERE " +
            "CASE " +
            "   WHEN :creditRating = 1 THEN l.credit1Interest BETWEEN :minRate AND :maxRate " +
            "   WHEN :creditRating = 2 THEN l.credit2Interest BETWEEN :minRate AND :maxRate " +
            "   WHEN :creditRating = 3 THEN l.credit3Interest BETWEEN :minRate AND :maxRate " +
            "   WHEN :creditRating = 4 THEN l.credit4Interest BETWEEN :minRate AND :maxRate " +
            "   WHEN :creditRating = 5 THEN l.credit5Interest BETWEEN :minRate AND :maxRate " +
            "END")
    List<Loan> findByInterestRateRange(
            @Param("creditRating") Integer creditRating,
            @Param("minRate") BigDecimal minRate,
            @Param("maxRate") BigDecimal maxRate
    );
}