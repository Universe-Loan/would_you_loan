package com.woorifisa.wl.service;

import com.woorifisa.wl.model.dto.AssetRecordDto;
import com.woorifisa.wl.model.entity.AssetRecord;
import com.woorifisa.wl.repository.AssetRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class AssetRecordService {
    private final AssetRecordRepository assetRecordRepository;

    @Autowired
    public AssetRecordService(AssetRecordRepository assetRecordRepository) {
        this.assetRecordRepository = assetRecordRepository;
    }

    public Map<String, List<AssetRecordDto>> getLastYearAssetData(Long userId) {
        // 현재 날짜 기준으로 1년치 데이터 조회
        LocalDate now = LocalDate.now();
        LocalDate oneYearAgo = now.minusYears(1);

        List<AssetRecord> records = assetRecordRepository.findByUserIdAndYearAndMonthGreaterThanEqual(
                userId,
                oneYearAgo.getYear(),
                oneYearAgo.getMonthValue()
        );

        List<AssetRecordDto> assetList = new ArrayList<>();
        List<AssetRecordDto> debtList = new ArrayList<>();

        for (AssetRecord record : records) {
            AssetRecordDto dto = new AssetRecordDto();
            dto.setRecentId(record.getRecentId());
            dto.setUserId(record.getUserId());
            dto.setAssetType(record.getAssetType());
            dto.setAmount(record.getAmount());
            dto.setYear(record.getYear());
            dto.setMonth(record.getMonth());

            if ("ASSET".equals(record.getAssetType())) {
                assetList.add(dto);
            } else if ("DEBT".equals(record.getAssetType())) {
                debtList.add(dto);
            }
        }

        Map<String, List<AssetRecordDto>> result = new HashMap<>();
        result.put("assets", assetList);
        result.put("debts", debtList);

        return result;
    }

}
