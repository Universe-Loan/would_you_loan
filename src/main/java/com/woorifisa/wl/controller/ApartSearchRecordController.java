package com.woorifisa.wl.controller;

import com.woorifisa.wl.model.dto.ApartSearchRecordDto;
import com.woorifisa.wl.model.entity.ApartSearchRecord;
import com.woorifisa.wl.service.ApartSearchRecordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/search-apt-record")
public class ApartSearchRecordController {
    private final ApartSearchRecordService apartSearchRecordService;

    public ApartSearchRecordController(ApartSearchRecordService apartSearchRecordService) {
        this.apartSearchRecordService = apartSearchRecordService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<ApartSearchRecordDto>> getSearchRecords(@PathVariable Long userId) {
        List<ApartSearchRecord> records = apartSearchRecordService.getSearchRecordsByUserId(userId);
        List<ApartSearchRecordDto> dtos = records.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @DeleteMapping("/{urlId}")
    public ResponseEntity<?> deleteSearchRecord(@PathVariable Long urlId, @RequestParam Long userId) {
        try {
            apartSearchRecordService.deleteSearchRecord(urlId, userId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting record");
        }
    }

    @PostMapping("/save-search")
    public ResponseEntity<?> saveSearchRecord(
            @RequestParam Long userId,
            @RequestParam String city,
            @RequestParam String district,
            @RequestParam String apartName,
            @RequestParam String url) {
        try {
            ApartSearchRecord savedRecord = apartSearchRecordService.saveSearchRecord(userId, city, district, apartName, url);
            return ResponseEntity.ok(convertToDto(savedRecord));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error saving search record: " + e.getMessage());
        }
    }

    @GetMapping("/top3")
    public ResponseEntity<List<Map<String, Object>>> getTop3Apartments() {
        return ResponseEntity.ok(apartSearchRecordService.getTop3SearchedApartments());
    }

    private ApartSearchRecordDto convertToDto(ApartSearchRecord record) {
        ApartSearchRecordDto dto = new ApartSearchRecordDto();
        dto.setUrlId(record.getUrlId());
        dto.setUserId(record.getUserId());
        dto.setApartName(record.getApartName());
        dto.setUrl(record.getUrl());
        return dto;
    }
}
