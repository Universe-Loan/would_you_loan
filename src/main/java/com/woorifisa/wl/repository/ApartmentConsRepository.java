package com.woorifisa.wl.repository;

import com.woorifisa.wl.model.entity.ApartmentCons;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ApartmentConsRepository extends JpaRepository<ApartmentCons, Long> {
    List<ApartmentCons> findByKaptCode(String kaptCode);
    Optional<ApartmentCons> findByUserIdAndKaptCode(Long userId, String kaptCode);
}
