package com.woorifisa.wl.service;

import com.woorifisa.wl.model.entity.ApartSearchRecord;
import com.woorifisa.wl.repository.ApartSearchRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

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

    public ApartSearchRecord saveSearchRecord(Long userId, String city, String district, String apartName, String url) {
        if (apartSearchRecordRepository.existsByUserIdAndUrl(userId, url)) {
            return null;
        }

        ApartSearchRecord record = new ApartSearchRecord();
        record.setUserId(userId);
        record.setCity(city);
        record.setDistrict(district);
        record.setApartName(apartName);
        record.setUrl(url);
        return apartSearchRecordRepository.save(record);
    }

    public List<Map<String, Object>> getTop3SearchedApartments() {
        return apartSearchRecordRepository.findTop3BySearchCount();
    }
}
