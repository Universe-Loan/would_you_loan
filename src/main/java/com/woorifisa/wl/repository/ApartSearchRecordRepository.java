package com.woorifisa.wl.repository;

import com.woorifisa.wl.model.entity.ApartSearchRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApartSearchRecordRepository extends JpaRepository<ApartSearchRecord, Long> {
    List<ApartSearchRecord> findByUserIdOrderByUrlIdDesc(Long userId);
    void deleteByUrlIdAndUserId(Long urlId, Long userId);

    // 추가: 같은 user_id와 url로 검색
    boolean existsByUserIdAndUrl(Long userId, String url);
}
