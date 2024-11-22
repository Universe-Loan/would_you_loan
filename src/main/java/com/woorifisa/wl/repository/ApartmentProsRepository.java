package com.woorifisa.wl.repository;

import com.woorifisa.wl.model.entity.ApartmentPros;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ApartmentProsRepository extends JpaRepository<ApartmentPros, Long> {
    List<ApartmentPros> findByKaptCode(String kaptCode);
    Optional<ApartmentPros> findByUserIdAndKaptCode(Long userId, String kaptCode);

}