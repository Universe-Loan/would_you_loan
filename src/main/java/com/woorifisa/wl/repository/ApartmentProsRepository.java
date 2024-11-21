package com.woorifisa.wl.repository;

import com.woorifisa.wl.model.entity.ApartmentPros;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApartmentProsRepository extends JpaRepository<ApartmentPros, Integer> {
    List<ApartmentPros> findByKaptCode(String kaptCode);
}