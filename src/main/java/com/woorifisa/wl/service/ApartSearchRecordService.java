package com.woorifisa.wl.service;

import com.woorifisa.wl.repository.ApartSearchRecordRepository;
import org.springframework.stereotype.Service;

@Service
public class ApartSearchRecordService {
    private final ApartSearchRecordRepository apartSearchRecordRepository;

    public ApartSearchRecordService(ApartSearchRecordRepository apartSearchRecordRepository) {
        this.apartSearchRecordRepository = apartSearchRecordRepository;
    }
}
