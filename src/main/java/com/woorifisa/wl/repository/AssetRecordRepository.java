package com.woorifisa.wl.repository;

import com.woorifisa.wl.model.entity.AssetRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssetRecordRepository extends JpaRepository<AssetRecord, Long> {
    @Query("SELECT a FROM AssetRecord a WHERE a.userId = :userId " +
            "AND ((a.year = :year AND a.month >= :month) " +
            "OR (a.year = :year + 1 AND a.month < :month)) " +
            "ORDER BY a.year, a.month")
    List<AssetRecord> findByUserIdAndYearAndMonthGreaterThanEqual(
            @Param("userId") Long userId,
            @Param("year") Integer year,
            @Param("month") Integer month
    );

    @Query("SELECT a FROM AssetRecord a WHERE a.userId = :userId AND a.year = :year AND a.month = :month AND a.assetType = :assetType")
    AssetRecord findByUserIdAndYearAndMonthAndAssetType(
            @Param("userId") Long userId,
            @Param("year") Integer year,
            @Param("month") Integer month,
            @Param("assetType") String assetType);
}