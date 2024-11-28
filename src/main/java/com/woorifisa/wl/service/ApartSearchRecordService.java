package com.woorifisa.wl.service;

import com.woorifisa.wl.model.entity.ApartSearchRecord;
import com.woorifisa.wl.repository.ApartSearchRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ApartSearchRecordService {
    private final ApartSearchRecordRepository apartSearchRecordRepository;

    public ApartSearchRecordService(ApartSearchRecordRepository apartSearchRecordRepository) {
        this.apartSearchRecordRepository = apartSearchRecordRepository;
    }

    public List<ApartSearchRecord> getSearchRecordsByUserId(Long userId) {
        return apartSearchRecordRepository.findByUserIdOrderByUrlIdDesc(userId);
    }

    public void deleteSearchRecord(Long urlId, Long userId) {
        apartSearchRecordRepository.deleteByUrlIdAndUserId(urlId, userId);
    }

    public ApartSearchRecord saveSearchRecord(Long userId, String apartName, String url) {
        // 중복 체크
        if (apartSearchRecordRepository.existsByUserIdAndUrl(userId, url)) {
            return null; // 또는 기존 레코드 반환
        }

        ApartSearchRecord record = new ApartSearchRecord();
        record.setUserId(userId);
        record.setApartName(apartName);
        record.setUrl(url);
        return apartSearchRecordRepository.save(record);
    }
}
