package com.woorifisa.wl.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.woorifisa.wl.model.dto.NewsArticleDto;
import com.woorifisa.wl.service.NewsArticleService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.InputStream;
import java.net.URI;
import java.util.*;

@Controller
@RequestMapping("/market-analysis")
public class MarketAnalysisController {
    private final NewsArticleService newsArticleService;
    private final RestTemplate restTemplate;

    public MarketAnalysisController(NewsArticleService newsArticleService, RestTemplate restTemplate) {
        this.newsArticleService = newsArticleService;
        this.restTemplate = restTemplate;
    }

    // 부동산 지표 api key
    @Value("${API_KEY_KOSIS_EH}")
    private String apiKeyKOSIS;

    // 부동산 지표
    @GetMapping("/indicators")
    public String showMarketAnalysis(Model model) {
        String[] apiUrls = {
                "https://kosis.kr/openapi/Param/statisticsParameterData.do?method=getList&apiKey=" + apiKeyKOSIS +
                        "&itmId=T1+&objL1=ALL&objL2=&objL3=&objL4=&objL5=&objL6=&objL7=&objL8=&format=json&jsonVD=Y&prdSe=M&newEstPrdCnt=120&prdInterval=1" +
                        "&outputFields=DT+PRD_SE+PRD_DE+C1_NM+ITM_NM+ITM_ID+C1_OBJ_NM+&orgId=390&tblId=DT_39002_01"
        };

        List<Map<String, Object>> results = new ArrayList<>();

        for (String apiUrl : apiUrls) {
            try {
                System.out.println("Sending request to: " + apiUrl);
                ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);

                if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                    System.out.println("API call successful for URL: " + apiUrl);
                    System.out.println("Response body: " + response.getBody());

                    // JSON 응답 파싱
                    ObjectMapper objectMapper = new ObjectMapper();
                    List<Map<String, Object>> apiData = objectMapper.readValue(response.getBody(), List.class);

                    if (apiData != null && !apiData.isEmpty()) {
                        results.add(Map.of("url", apiUrl, "data", apiData));
                    } else {
                        System.err.println("No data in response for URL: " + apiUrl);
                    }
                } else {
                    System.err.println("API call failed for URL: " + apiUrl + ". Response code: " + response.getStatusCode());
                }
            } catch (Exception e) {
                System.err.println("Error during API call for URL: " + apiUrl + ". Exception: " + e.getMessage());
            }
        }

        try {
            // JSON 직렬화하여 클라이언트로 전달
            ObjectMapper objectMapper = new ObjectMapper();
            String apiResultsJson = objectMapper.writeValueAsString(results);
            model.addAttribute("apiResultsJson", apiResultsJson);
            System.out.println("Serialized JSON: " + apiResultsJson);
        } catch (Exception e) {
            System.err.println("Error serializing results: " + e.getMessage());
            model.addAttribute("apiResultsJson", "[]");
        }

        return "market_analysis"; // View 이름
    }

    // 부동산 기사
    @GetMapping
    public String getArticles(@RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size,
                              Model model) {
        Page<NewsArticleDto> articlesPage = newsArticleService.getPaginatedArticles(page, size);
        int totalPages = articlesPage.getTotalPages();
        int currentPage = page;

        // 페이지 번호 범위 계산 (7개씩 표시)
        int pageRange = 7; // 보여줄 페이지 번호 개수
        int startPage = Math.max(0, currentPage - pageRange / 2);
        int endPage = Math.min(totalPages - 1, startPage + pageRange - 1);

        // 시작 페이지 보정
        if ((endPage - startPage) < (pageRange - 1)) {
            startPage = Math.max(0, endPage - pageRange + 1);
        }

        model.addAttribute("articles", articlesPage.getContent()); // 현재 페이지 데이터
        model.addAttribute("currentPage", currentPage); // 현재 페이지
        model.addAttribute("totalPages", totalPages); // 총 페이지 수
        model.addAttribute("startPage", startPage); // 시작 페이지 번호
        model.addAttribute("endPage", endPage); // 끝 페이지 번호

        return "market_analysis"; // 렌더링할 템플릿 이름
    }

    // 부동산 날씨
    @GetMapping("/geojson-weather")
    @ResponseBody
    public ResponseEntity<?> getGeoJsonWithWeather() {
        try {
            // GeoJSON 파일 읽기
            Map<String, Object> geoJsonData;
            String geoJsonPath = "static/geojson/administrative_districts_simplified.geojson";
            InputStream geoJsonStream = new ClassPathResource(geoJsonPath).getInputStream();
            ObjectMapper objectMapper = new ObjectMapper();
            geoJsonData = objectMapper.readValue(geoJsonStream, Map.class);
            System.out.println("GeoJSON data successfully loaded.");

            // 모든 SIG_CD를 추출하여 날씨 데이터 호출
            Map<String, String> weatherMap = new HashMap<>();
            if (geoJsonData != null && geoJsonData.containsKey("features")) {
                List<Map<String, Object>> features = (List<Map<String, Object>>) geoJsonData.get("features");
                Set<String> sigCdSet = new HashSet<>();

                // 모든 SIG_CD를 추출
                for (Map<String, Object> feature : features) {
                    Map<String, Object> properties = (Map<String, Object>) feature.get("properties");
                    String sigCd = (String) properties.get("SIG_CD");
                    if (sigCd != null) {
                        sigCdSet.add(sigCd);
                    }
                }

                System.out.println("Extracted SIG_CD values: " + sigCdSet);

                // 각 SIG_CD에 대해 날씨 데이터 호출
                for (String sigCd : sigCdSet) {
                    String weather = fetchWeatherDataForSigCd(sigCd);
                    if (weather != null) {
                        weatherMap.put(sigCd, weather);
                        System.out.println("Fetched weather for SIG_CD " + sigCd + ": " + weather);
                    }
                }
            }

            // GeoJSON 데이터와 날씨 정보 병합
            // 병렬 스트림을 사용한 방법
            if (geoJsonData != null && geoJsonData.containsKey("features")) {
                List<Map<String, Object>> features = (List<Map<String, Object>>) geoJsonData.get("features");

                features.parallelStream().forEach(feature -> {
                    Map<String, Object> properties = (Map<String, Object>) feature.get("properties");
                    String sigCd = (String) properties.get("SIG_CD");
                    System.out.println("Processing SIG_CD: " + sigCd);

                    // 날씨 정보 추가
                    String weather = weatherMap.getOrDefault(sigCd, "정보 없음");
                    System.out.println("Weather for SIG_CD " + sigCd + ": " + weather);
                    properties.put("weather", weather);
                });
            }

            System.out.println("GeoJSON and weather data successfully merged.");
            return ResponseEntity.ok(geoJsonData);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing GeoJSON and weather data: " + e.getMessage());
        }
    }

    /**
     * 특정 SIG_CD에 대해 날씨 데이터를 호출합니다.
     */
    public String fetchWeatherDataForSigCd(String sigCd) {
        try {
            System.out.println("Fetching weather data for SIG_CD: " + sigCd);
            String url = "https://data-api.kbland.kr/bfmstat/wthrchat/husePrcIndx";
            UriComponentsBuilder weatherUri = UriComponentsBuilder.fromHttpUrl(url)
                    .queryParam("월간주간구분코드", "01")
                    .queryParam("매매전세코드", "01")
                    .queryParam("매물종별구분", "01")
                    .queryParam("면적크기코드", "00")
                    .queryParam("단위구분코드", "01")
                    .queryParam("법정동코드", sigCd)
                    .queryParam("지역명", "전국")
                    .queryParam("시도명", "전국")
                    .queryParam("조회시작일자", "202410")
                    .queryParam("조회종료일자", "202410")
                    .queryParam("selectedTab", "0")
                    .queryParam("changeRatio", "true")
                    .queryParam("mapType", "false")
                    .queryParam("페이지번호", "")
                    .queryParam("페이지목록수", "")
                    .queryParam("zoomLevel", "8")
                    .queryParam("탭구분코드", "0");



            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity entity = new HttpEntity(headers);
            ResponseEntity<Map> response = restTemplate.exchange(
                    weatherUri.build().encode().toUri(), // 한글 깨짐 현상 방지
                    HttpMethod.GET,
                    entity,
                    Map.class);

            Map<String, Object> data = response.getBody();

            String weather = "☁️"; // 기본값
            if (data != null && data.containsKey("dataBody")) {
                Map<String, Object> dataBody = (Map<String, Object>) data.get("dataBody");

                if (dataBody.containsKey("data")) {
                    Map<String, Object> dataMap = (Map<String, Object>) dataBody.get("data");

                    if (dataMap.containsKey("depth1")) {
                        List<Map<String, Object>> depth1 = (List<Map<String, Object>>) dataMap.get("depth1");
//                        System.out.println("Depth1: " + depth1);

                        for (Map<String, Object> item : depth1) {
                            System.out.println(item);
                            if (item.get("법정동코드").toString().startsWith(sigCd)) {
                                if (item.get("변동률") == null) {
                                    weather = "☁️";
                                } else {
                                    double changeRate = Double.parseDouble(item.get("변동률").toString());
                                    System.out.println("Change Rate: " + changeRate);
                                    weather = changeRate < 0 ? "☁️" : "☀️";
                                    break;
                                }
                            }
                        }
                    }
                }
            }

            return weather;

        } catch (Exception e) {
            System.err.println("Error fetching weather data for SIG_CD " + sigCd + ": " + e.getMessage());
            e.printStackTrace();
            return "정보 없음";
        }
    }

}
