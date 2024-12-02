package com.woorifisa.wl.repository;

import com.woorifisa.wl.model.entity.VerificationResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationResultRepository extends JpaRepository<VerificationResult, Long> {
}
