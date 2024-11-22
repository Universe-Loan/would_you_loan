package com.woorifisa.wl.repository;

import com.woorifisa.wl.model.dto.ApartmentProsDto;
import com.woorifisa.wl.model.entity.ApartmentPros;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ApartmentProsRepository extends JpaRepository<ApartmentPros, Long> {
    List<ApartmentPros> findByKaptCode(String kaptCode);
    Optional<ApartmentPros> findByUserIdAndKaptCode(Long userId, String kaptCode);

    @Query("SELECT new com.woorifisa.wl.model.dto.ApartmentProsDto(" +
            "a.kaptCode, " +
            "COUNT(CASE WHEN a.quiet = true THEN 1 ELSE NULL END), " +
            "COUNT(CASE WHEN a.transport = true THEN 1 ELSE NULL END), " +
            "COUNT(CASE WHEN a.parking = true THEN 1 ELSE NULL END), " +
            "COUNT(CASE WHEN a.security = true THEN 1 ELSE NULL END), " +
            "COUNT(CASE WHEN a.maintenance = true THEN 1 ELSE NULL END), " +
            "COUNT(CASE WHEN a.neighbors = true THEN 1 ELSE NULL END), " +
            "COUNT(CASE WHEN a.school = true THEN 1 ELSE NULL END), " +
            "COUNT(CASE WHEN a.community = true THEN 1 ELSE NULL END), " +
            "COUNT(CASE WHEN a.commercial = true THEN 1 ELSE NULL END), " +
            "COUNT(CASE WHEN a.daycare = true THEN 1 ELSE NULL END), " +
            "COUNT(CASE WHEN a.medical = true THEN 1 ELSE NULL END), " +
            "COUNT(CASE WHEN a.restaurants = true THEN 1 ELSE NULL END), " +
            "COUNT(CASE WHEN a.parks = true THEN 1 ELSE NULL END)) " +
            "FROM ApartmentPros a " +
            "WHERE a.kaptCode = :kaptCode " +
            "GROUP BY a.kaptCode")
    ApartmentProsDto getProsCounts(@Param("kaptCode") String kaptCode);
}