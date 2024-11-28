package com.woorifisa.wl.repository;

import com.woorifisa.wl.model.entity.ApartSearchRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface ApartSearchRecordRepository extends JpaRepository<ApartSearchRecord, Long> {
    List<ApartSearchRecord> findByUserIdOrderByUrlIdDesc(Long userId);
    void deleteByUrlIdAndUserId(Long urlId, Long userId);

    // 추가: 같은 user_id와 url로 검색
    boolean existsByUserIdAndUrl(Long userId, String url);

    @Query("SELECT new map(a.city as city, a.district as district, a.apartName as apartName, COUNT(a) as count, a.url as url) " +
            "FROM ApartSearchRecord a " +
            "GROUP BY a.city, a.district, a.apartName, a.url " +
            "ORDER BY COUNT(a) DESC " +
            "LIMIT 3")
    List<Map<String, Object>> findTop3BySearchCount();
}
