package com.woorifisa.wl.service;

import com.woorifisa.wl.model.dto.ApartmentConsDto;
import com.woorifisa.wl.model.dto.ApartmentProsDto;
import com.woorifisa.wl.model.entity.ApartmentCons;
import com.woorifisa.wl.model.entity.ApartmentPros;
import com.woorifisa.wl.repository.ApartmentConsRepository;
import com.woorifisa.wl.repository.ApartmentProsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ApartmentEvaluationService {
    private final ApartmentProsRepository apartmentProsRepository;
    private final ApartmentConsRepository apartmentConsRepository;

    // 특정 아파트의 장단점 평가 카운트 조회
    @Transactional(readOnly = true)
    public Map<String, Object> getApartmentEvaluation(String kaptCode) {
        // 장점과 단점 데이터 조회
        ApartmentProsDto prosDto = apartmentProsRepository.getProsCounts(kaptCode);
        ApartmentConsDto consDto = apartmentConsRepository.getConsCounts(kaptCode);

        // 평가 데이터 종합
        Map<String, Object> evaluation = new HashMap<>();
        evaluation.put("prosCounts", prosDto);
        evaluation.put("consCounts", consDto);

        return evaluation;
    }

    // 장점 관련 메서드들
    public boolean hasProsEvaluation(String kaptCode, Long userId) {
        return apartmentProsRepository.findByUserIdAndKaptCode(userId, kaptCode).isPresent();
    }

    @Transactional
    public void saveNewProsEvaluation(String kaptCode, Long userId, List<String> prosList) {
        ApartmentPros pros = new ApartmentPros();
        pros.setKaptCode(kaptCode);
        pros.setUserId(userId);
        setApartmentProsFields(pros, prosList);
        apartmentProsRepository.save(pros);
    }

    @Transactional
    public void updateProsEvaluation(String kaptCode, Long userId, List<String> prosList) {
        apartmentProsRepository.findByUserIdAndKaptCode(userId, kaptCode)
                .ifPresent(pros -> {
                    setApartmentProsFields(pros, prosList);
                    apartmentProsRepository.save(pros);
                });
    }

    // 단점 관련 메서드들
    public boolean hasConsEvaluation(String kaptCode, Long userId) {
        return apartmentConsRepository.findByUserIdAndKaptCode(userId, kaptCode).isPresent();
    }

    @Transactional
    public void saveNewConsEvaluation(String kaptCode, Long userId, List<String> consList) {
        ApartmentCons cons = new ApartmentCons();
        cons.setKaptCode(kaptCode);
        cons.setUserId(userId);
        setApartmentConsFields(cons, consList);
        apartmentConsRepository.save(cons);
    }

    @Transactional
    public void updateConsEvaluation(String kaptCode, Long userId, List<String> consList) {
        apartmentConsRepository.findByUserIdAndKaptCode(userId, kaptCode)
                .ifPresent(cons -> {
                    setApartmentConsFields(cons, consList);
                    apartmentConsRepository.save(cons);
                });
    }

    private void setApartmentProsFields(ApartmentPros pros, List<String> selectedPros) {
        pros.setQuiet(selectedPros.contains("조용해요"));
        pros.setTransport(selectedPros.contains("교통이 편리해요"));
        pros.setParking(selectedPros.contains("주차가 편리해요"));
        pros.setSecurity(selectedPros.contains("보안이 좋아요"));
        pros.setMaintenance(selectedPros.contains("관리 상태가 좋아요"));
        pros.setNeighbors(selectedPros.contains("이웃들이 좋아요"));
        pros.setSchool(selectedPros.contains("학군이 좋아요"));
        pros.setCommunity(selectedPros.contains("커뮤니티 시설이 잘 되어 있어요"));
        pros.setCommercial(selectedPros.contains("상가가 잘 되어 있어요"));
        pros.setDaycare(selectedPros.contains("단지 내 유치원/어린이집이 있어요"));
        pros.setMedical(selectedPros.contains("주변 의료 환경이 좋아요"));
        pros.setRestaurants(selectedPros.contains("근처에 맛집이 많아요"));
        pros.setParks(selectedPros.contains("산책로/공원이 잘 되어 있어요"));
    }

    private void setApartmentConsFields(ApartmentCons cons, List<String> selectedCons) {
        cons.setNoise(selectedCons.contains("소음이 많아요"));
        cons.setTransport(selectedCons.contains("교통이 불편해요"));
        cons.setParking(selectedCons.contains("주차가 불편해요"));
        cons.setSecurity(selectedCons.contains("외부인 출입이 자유로워요"));
        cons.setOutdated(selectedCons.contains("노후된 부분이 많아요"));
        cons.setSchool(selectedCons.contains("학교가 멀어요"));
        cons.setCommunity(selectedCons.contains("커뮤니티 시설이 없어요"));
        cons.setCommercial(selectedCons.contains("상가가 없어요"));
        cons.setDaycare(selectedCons.contains("단지 내 유치원/어린이집이 없어요"));
        cons.setMedical(selectedCons.contains("주변 의료 환경이 불편해요"));
        cons.setRestaurants(selectedCons.contains("식당이 멀어요"));
        cons.setParks(selectedCons.contains("근처에 산책로/공원이 없어요"));
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getUserEvaluation(String kaptCode, Long userId) {
        Map<String, Object> result = new HashMap<>();

        // 장점 평가 여부 확인
        boolean hasProsEvaluation = hasProsEvaluation(kaptCode, userId);
        result.put("hasProsEvaluation", hasProsEvaluation);

        // 단점 평가 여부 확인
        boolean hasConsEvaluation = hasConsEvaluation(kaptCode, userId);
        result.put("hasConsEvaluation", hasConsEvaluation);

        // 각각의 평가 내용 조회
        List<String> pros = new ArrayList<>();
        List<String> cons = new ArrayList<>();

        // 장점 조회
        if (hasProsEvaluation) {
            apartmentProsRepository.findByUserIdAndKaptCode(userId, kaptCode).ifPresent(p -> {
                if (p.getQuiet()) pros.add("조용해요");
                if (p.getTransport()) pros.add("교통이 편리해요");
                if (p.getParking()) pros.add("주차가 편리해요");
                if (p.getSecurity()) pros.add("보안이 좋아요");
                if (p.getMaintenance()) pros.add("관리 상태가 좋아요");
                if (p.getNeighbors()) pros.add("이웃들이 좋아요");
                if (p.getSchool()) pros.add("학군이 좋아요");
                if (p.getCommunity()) pros.add("커뮤니티 시설이 잘 되어 있어요");
                if (p.getCommercial()) pros.add("상가가 잘 되어 있어요");
                if (p.getDaycare()) pros.add("단지 내 유치원/어린이집이 있어요");
                if (p.getMedical()) pros.add("주변 의료 환경이 좋아요");
                if (p.getRestaurants()) pros.add("근처에 맛집이 많아요");
                if (p.getParks()) pros.add("산책로/공원이 잘 되어 있어요");
            });
        }

        // 단점 조회
        if (hasConsEvaluation) {
            apartmentConsRepository.findByUserIdAndKaptCode(userId, kaptCode).ifPresent(c -> {
                if (c.getNoise()) cons.add("소음이 많아요");
                if (c.getTransport()) cons.add("교통이 불편해요");
                if (c.getParking()) cons.add("주차가 불편해요");
                if (c.getSecurity()) cons.add("외부인 출입이 자유로워요");
                if (c.getOutdated()) cons.add("노후된 부분이 많아요");
                if (c.getSchool()) cons.add("학교가 멀어요");
                if (c.getCommunity()) cons.add("커뮤니티 시설이 없어요");
                if (c.getCommercial()) cons.add("상가가 없어요");
                if (c.getDaycare()) cons.add("단지 내 유치원/어린이집이 없어요");
                if (c.getMedical()) cons.add("주변 의료 환경이 불편해요");
                if (c.getRestaurants()) cons.add("식당이 멀어요");
                if (c.getParks()) cons.add("근처에 산책로/공원이 없어요");
            });
        }

        result.put("pros", pros);
        result.put("cons", cons);

        return result;
    }
}