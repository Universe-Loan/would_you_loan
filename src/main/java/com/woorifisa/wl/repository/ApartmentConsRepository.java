package com.woorifisa.wl.repository;

import com.woorifisa.wl.model.entity.ApartmentCons;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApartmentConsRepository extends JpaRepository<ApartmentCons, Integer> {
    List<ApartmentCons> findByKaptCode(String kaptCode);
}
