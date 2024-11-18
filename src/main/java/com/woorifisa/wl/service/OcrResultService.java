package com.woorifisa.wl.service;

import com.woorifisa.wl.model.dto.OcrResultDto;
import com.woorifisa.wl.model.entity.OcrResult;
import com.woorifisa.wl.repository.OcrResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OcrResultService {

    private final OcrResultRepository ocrResultRepository;

    @Autowired
    public OcrResultService(OcrResultRepository ocrResultRepository) {
        this.ocrResultRepository = ocrResultRepository;
    }

    public void addOcrResult(OcrResultDto ocrResultDto) {
        // DTO를 Entity로 변환
        OcrResult ocrResult = new OcrResult();
        ocrResult.setLocation(ocrResultDto.getLocation());
        ocrResult.setBuildingUse(ocrResultDto.getBuildingUse());
        ocrResult.setExclusiveArea(ocrResultDto.getExclusiveArea());
        ocrResult.setSalePrice(ocrResultDto.getSalePrice());
        ocrResult.setBuyerAddress(ocrResultDto.getBuyerAddress());
        ocrResult.setBuyerName(ocrResultDto.getBuyerName());

        // DB에 저장
        ocrResultRepository.save(ocrResult);
    }

    public void updateOcrResult(OcrResultDto ocrResultDto) {
        // 예를 들어, ID 1번 데이터를 수정한다고 가정
        OcrResult ocrResult = ocrResultRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("OCR result not found"));

        // DTO 데이터를 Entity에 매핑
        ocrResult.setLocation(ocrResultDto.getLocation());
        ocrResult.setBuildingUse(ocrResultDto.getBuildingUse());
        ocrResult.setExclusiveArea(ocrResultDto.getExclusiveArea());
        ocrResult.setSalePrice(ocrResultDto.getSalePrice());
        ocrResult.setBuyerAddress(ocrResultDto.getBuyerAddress());
        ocrResult.setBuyerName(ocrResultDto.getBuyerName());

        // 업데이트 수행
        ocrResultRepository.save(ocrResult);
    }
}
