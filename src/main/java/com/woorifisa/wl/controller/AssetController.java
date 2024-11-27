package com.woorifisa.wl.controller;

import com.woorifisa.wl.model.dto.AssetRecordDto;
import com.woorifisa.wl.service.AssetRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/asset")
public class AssetController {
    private final AssetRecordService assetRecordService;

    @Autowired
    public AssetController(AssetRecordService assetRecordService) {
        this.assetRecordService = assetRecordService;
    }

    @GetMapping("/chart")
    public ResponseEntity<Map<String, List<AssetRecordDto>>> getAssetChartData(@RequestParam Long userId) {
        return ResponseEntity.ok(assetRecordService.getLastYearAssetData(userId));
    }
}
