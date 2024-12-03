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
import java.time.YearMonth;
import java.util.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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

    // CD금리 api key
    @Value("${API_KEY_ECOS_EH}")
    private String apiKeyECOS;

    // 매매가격지수 api key
    @Value("${API_KEY_RONE_EH}")
    private String apiKeyRONE;

    // 부동산 기사, 지표
    @GetMapping
    public String getArticles(@RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size,
                              Model model) {
        // 기사
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


        // 지표3
        // newEstPrdCnt가 데이터 기간 설정
        String[] apiUrls = {
                "https://kosis.kr/openapi/Param/statisticsParameterData.do?method=getList&apiKey=" + apiKeyKOSIS +
                        "&itmId=T1+&objL1=ALL&objL2=&objL3=&objL4=&objL5=&objL6=&objL7=&objL8=&format=json&jsonVD=Y&prdSe=M&newEstPrdCnt=12&prdInterval=1" +
                        "&outputFields=OBJ_ID+OBJ_NM+OBJ_NM_ENG+NM+NM_ENG+ITM_ID+ITM_NM+ITM_NM_ENG+UNIT_NM+UNIT_NM_ENG+PRD_SE+PRD_DE+&orgId=390&tblId=DT_39002_01",
                "https://kosis.kr/openapi/Param/statisticsParameterData.do?method=getList&apiKey=" + apiKeyKOSIS +
                        "&itmId=T1+&objL1=ALL&objL2=&objL3=&objL4=&objL5=&objL6=&objL7=&objL8=&format=json&jsonVD=Y&prdSe=M&newEstPrdCnt=12&prdInterval=1" +
                        "&outputFields=OBJ_ID+OBJ_NM+OBJ_NM_ENG+NM+NM_ENG+ITM_ID+ITM_NM+ITM_NM_ENG+UNIT_NM+UNIT_NM_ENG+PRD_SE+PRD_DE+&orgId=390&tblId=DT_39002_02",
                "https://kosis.kr/openapi/Param/statisticsParameterData.do?method=getList&apiKey=" + apiKeyKOSIS +
                        "&itmId=T1+&objL1=ALL&objL2=&objL3=&objL4=&objL5=&objL6=&objL7=&objL8=&format=json&jsonVD=Y&prdSe=M&newEstPrdCnt=12&prdInterval=1" +
                        "&outputFields=OBJ_ID+OBJ_NM+OBJ_NM_ENG+NM+NM_ENG+ITM_ID+ITM_NM+ITM_NM_ENG+UNIT_NM+UNIT_NM_ENG+PRD_SE+PRD_DE+&orgId=390&tblId=DT_39002_04",
                "https://kosis.kr/openapi/Param/statisticsParameterData.do?method=getList&apiKey=" + apiKeyKOSIS +
                        "&itmId=T001+&objL1=ALL&objL2=ALL&objL3=&objL4=&objL5=&objL6=&objL7=&objL8=&format=json&jsonVD=Y&prdSe=M&newEstPrdCnt=1&prdInterval=1" +
                        "&outputFields=OBJ_ID+OBJ_NM+OBJ_NM_ENG+NM+NM_ENG+ITM_ID+ITM_NM+ITM_NM_ENG+UNIT_NM+UNIT_NM_ENG+PRD_SE+PRD_DE+&orgId=390&tblId=DT_39002_08"
        };

        List<Map<String, Object>> results = new ArrayList<>();

        for (String apiUrl : apiUrls) {
            try {
                ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);

                if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    List<Map<String, Object>> apiData = objectMapper.readValue(response.getBody(), List.class);

                    if (apiData != null && !apiData.isEmpty()) {
                        results.add(Map.of("url", apiUrl, "data", apiData));
                    } else {
                        System.err.println("API 응답에 데이터가 없습니다. URL: " + apiUrl);
                    }
                } else {
                    System.err.println("API 호출 실패. URL: " + apiUrl + ". 응답 코드: " + response.getStatusCode());
                }
            } catch (Exception e) {
                System.err.println("API 호출 중 오류 발생. URL: " + apiUrl + ". 예외: " + e.getMessage());
            }
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String apiResultsJson = objectMapper.writeValueAsString(results);
            model.addAttribute("apiResultsJson", apiResultsJson);
        } catch (Exception e) {
            System.err.println("결과 직렬화 중 오류 발생: " + e.getMessage());
            model.addAttribute("apiResultsJson", "[]");
        }

    // CD 금리 데이터 처리

        // 날짜 계산
        LocalDate now = LocalDate.now();
        LocalDate NYearsAgo = now.minusYears(1); // 현재로부터 n년
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMM");
        String startDate = NYearsAgo.format(formatter);
        String endDate = now.format(formatter);
        String cdRateUrl = "https://ecos.bok.or.kr/api/StatisticSearch/" + apiKeyECOS + "/json/kr/1/12/721Y001/M/" + startDate + "/" + endDate + "/2010000";

        try {
            ResponseEntity<Map> response = restTemplate.getForEntity(cdRateUrl, Map.class);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> root = response.getBody();
                Map<String, Object> statisticSearch = (Map<String, Object>) root.get("StatisticSearch");
                List<Map<String, Object>> rows = (List<Map<String, Object>>) statisticSearch.get("row");
                model.addAttribute("cdRateData", rows);
            } else {
                model.addAttribute("cdRateData", new ArrayList<>());
            }
        } catch (Exception e) {
            model.addAttribute("cdRateData", new ArrayList<>());
            System.err.println("CD 금리 데이터 호출 오류: " + e.getMessage());
        }

    // CD 금리 예측 결과값
        // API URL
        String url = "http://regularmark.iptime.org:30049/predict-interest-direction";

        try {
            // API 호출
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                // 응답 데이터 처리
                Map<String, Object> responseBody = response.getBody();

                // `data` 추출 및 변환
                List<Map<String, Object>> data = (List<Map<String, Object>>) responseBody.get("data");

                // 데이터 처리
                List<Map<String, Object>> processedData = new ArrayList<>();
                DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM");
                DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy년 MM월");

                for (Map<String, Object> item : data) {
                    Map<String, Object> processedItem = new HashMap<>();

                    // 날짜 처리 (last_data_point)
                    String lastDataPoint = String.valueOf(item.get("last_data_point"));
                    try {
                        // `YearMonth`로 파싱
                        YearMonth yearMonth = YearMonth.parse(lastDataPoint, inputFormatter);
                        processedItem.put("last_data_point", yearMonth.format(outputFormatter));
                    } catch (Exception e) {
                        System.err.println("날짜 변환 실패: " + lastDataPoint);
                        processedItem.put("last_data_point", lastDataPoint); // 변환 실패 시 원래 값 사용
                    }

                    // 숫자 처리 (probability)
                    Object probability = item.get("probability");
                    if (probability instanceof Number) {
                        processedItem.put("probability", ((Number) probability).doubleValue());
                    } else {
                        processedItem.put("probability", 0.0); // 기본값
                    }

                    // 텍스트 처리 (prediction)
                    processedItem.put("prediction", item.get("prediction"));

                    processedData.add(processedItem);
                }

                // 모델에 변환된 데이터 추가
                model.addAttribute("data", processedData);
            } else {
                System.err.println("API 호출 실패: " + response.getStatusCode());
                model.addAttribute("error", "API 호출에 실패했습니다.");
            }
        } catch (Exception e) {
            System.err.println("API 호출 중 오류 발생: " + e.getMessage());
            model.addAttribute("error", "API 호출 중 오류가 발생했습니다.");
        }

    // 아파트 매매지수
        String baseUrl = "https://www.reb.or.kr/r-one/openapi/SttsApiTblData.do";

        // CLS_ID 500008(서울), 500009(경기)에 대한 데이터를 가져오기
        List<String> clsIds = List.of("500007", "500015");

        List<Map<String, Object>> allData = new ArrayList<>();

        for (String clsId : clsIds) {
            String priceUrl = baseUrl + "?KEY=" + apiKeyRONE +
                    "&Type=json&pIndex=1&pSize=50&STATBL_ID=A_2024_00178&DTACYCLE_CD=MM&CLS_ID="
                    + clsId + "&ITM_ID=100001&START_WRTTIME=" + startDate + "&END_WRTTIME=" + endDate;

            try {
                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<String> response = restTemplate.getForEntity(priceUrl, String.class);

                if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                    // JSON 응답을 수동으로 처리
                    ObjectMapper objectMapper = new ObjectMapper();
                    Map<String, Object> responseBody = objectMapper.readValue(response.getBody(), Map.class);

                    List<Map<String, Object>> rows = (List<Map<String, Object>>)
                            ((Map<String, Object>) ((List<?>) responseBody.get("SttsApiTblData")).get(1)).get("row");

                    for (Map<String, Object> row : rows) {
                        allData.add(Map.of(
                                "date", row.get("WRTTIME_IDTFR_ID"),
                                "value", row.get("DTA_VAL"),
                                "region", row.get("CLS_NM")
                        ));
                    }
                } else {
                    System.err.println("API 호출 실패: " + response.getStatusCode());
                }
            } catch (Exception e) {
                System.err.println("API 호출 중 오류 발생: " + e.getMessage());
            }
        }

        // 데이터를 모델에 추가
        model.addAttribute("aptPriceIndicatorData", allData);


    // 아파트 매매지수 예측
        String seoulUrl = "http://regularmark.iptime.org:30049/predict-apt";
        String gyeonggiUrl = "http://regularmark.iptime.org:30049/predict-apt-gyeonggi";

        RestTemplate restTemplate = new RestTemplate();

        try {
            // 서울 예측 데이터 호출
            ResponseEntity<Map> aptResponse = restTemplate.getForEntity(seoulUrl, Map.class);
            if (aptResponse.getStatusCode().is2xxSuccessful() && aptResponse.getBody() != null) {
                List<Map<String, Object>> aptData = (List<Map<String, Object>>) aptResponse.getBody().get("data");
                model.addAttribute("seoulData", aptData); // "aptData"로 모델에 추가
            } else {
                model.addAttribute("seoulData", new ArrayList<>()); // 빈 데이터 처리
                System.err.println("서울 예측 API 호출 실패: " + aptResponse.getStatusCode());
            }

            // 경기 예측 데이터 호출
            ResponseEntity<Map> gyeonggiResponse = restTemplate.getForEntity(gyeonggiUrl, Map.class);
            if (gyeonggiResponse.getStatusCode().is2xxSuccessful() && gyeonggiResponse.getBody() != null) {
                List<Map<String, Object>> gyeonggiData = (List<Map<String, Object>>) gyeonggiResponse.getBody().get("data");
                model.addAttribute("gyeonggiData", gyeonggiData); // "gyeonggiData"로 모델에 추가
            } else {
                model.addAttribute("gyeonggiData", new ArrayList<>()); // 빈 데이터 처리
                System.err.println("경기 예측 API 호출 실패: " + gyeonggiResponse.getStatusCode());
            }
        } catch (Exception e) {
            System.err.println("API 호출 중 오류 발생: " + e.getMessage());
            model.addAttribute("seoulData", new ArrayList<>());
            model.addAttribute("gyeonggiData", new ArrayList<>());
        }

        return "market_analysis";
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
//            System.out.println("GeoJSON data successfully loaded.");

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

//                System.out.println("Extracted SIG_CD values: " + sigCdSet);

                // 각 SIG_CD에 대해 날씨 데이터 호출
                for (String sigCd : sigCdSet) {
                    String weather = fetchWeatherDataForSigCd(sigCd);
                    if (weather != null) {
                        weatherMap.put(sigCd, weather);
//                        System.out.println("Fetched weather for SIG_CD " + sigCd + ": " + weather);
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
//                    System.out.println("Processing SIG_CD: " + sigCd);

                    // 날씨 정보 추가
                    String weather = weatherMap.getOrDefault(sigCd, "정보 없음");
//                    System.out.println("Weather for SIG_CD " + sigCd + ": " + weather);
                    properties.put("weather", weather);
                });
            }

//            System.out.println("GeoJSON and weather data successfully merged.");
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
//            System.out.println("Fetching weather data for SIG_CD: " + sigCd);
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
//                            System.out.println(item);
                            if (item.get("법정동코드").toString().startsWith(sigCd)) {
                                if (item.get("변동률") == null) {
                                    weather = "☁️";
                                } else {
                                    double changeRate = Double.parseDouble(item.get("변동률").toString());
//                                    System.out.println("Change Rate: " + changeRate);
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
