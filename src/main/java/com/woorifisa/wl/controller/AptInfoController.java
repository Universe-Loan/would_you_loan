package com.woorifisa.wl.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.StandardCharsets;
import java.net.URI;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class AptInfoController {

    @Value("${API_KEY_DATA_EH}")
    private String serviceKey;
    private final String API_BASE_URL = "http://apis.data.go.kr/1613000/RTMSDataSvcAptTradeDev/getRTMSDataSvcAptTradeDev";

    @GetMapping("/apt-find")
    public String showAptFind(Model model) {
        // apt_find 페이지 로직
        return "apt_find";
    }

    @GetMapping("/address/popup")
    public String showAddressPopup() {
        return "real_address_popup";
    }

    @PostMapping("/search-apartment")
    public String searchApartment(@RequestParam String city,
                                  @RequestParam String district,
                                  @RequestParam String neighborhood,
                                  @RequestParam String apartment,
                                  @RequestParam String lawdCd,
                                  RedirectAttributes redirectAttributes) {

        redirectAttributes.addAttribute("city", city);
        redirectAttributes.addAttribute("district", district);
        redirectAttributes.addAttribute("neighborhood", neighborhood);
        redirectAttributes.addAttribute("apartment", apartment);
        redirectAttributes.addAttribute("lawdCd", lawdCd);

        return "redirect:/apt-report";
    }

    @GetMapping(value = "/apt-report", produces = MediaType.APPLICATION_XML_VALUE)
    public String showAptReport(@RequestParam(required = false) String city,
                                @RequestParam(required = false) String district,
                                @RequestParam(required = false) String neighborhood,
                                @RequestParam(required = false) String apartment,
                                @RequestParam(required = false) String lawdCd,
                                @RequestParam(required = false, defaultValue = "1") int pageNo,
                                @RequestParam(required = false, defaultValue = "10") int numOfRows,
                                Model model) {

        System.out.println("주소API Received parameters:");
        System.out.println("City: " + city);
        System.out.println("District: " + district);
        System.out.println("Neighborhood: " + neighborhood);
        System.out.println("Apartment: " + apartment);
        System.out.println("lawdCd: " + lawdCd);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));

        // n년치 데이터 불러오기
        List<Map<String, String>> allItems = new ArrayList<>();
        LocalDate currentDate = LocalDate.now();
        LocalDate NYearAgo = currentDate.minusYears(1);

        for (LocalDate date = NYearAgo; date.isBefore(currentDate) || date.isEqual(currentDate); date = date.plusMonths(1)) {
            String currentDealYmd = date.format(DateTimeFormatter.ofPattern("yyyyMM"));

            URI uri = UriComponentsBuilder.fromHttpUrl(API_BASE_URL)
                    .queryParam("serviceKey", serviceKey)
                    .queryParam("LAWD_CD", lawdCd)
                    .queryParam("DEAL_YMD", currentDealYmd)
                    .queryParam("pageNo", pageNo)
                    .queryParam("numOfRows", numOfRows)
                    .build(true)
                    .toUri();

            System.out.println("API 호출 URL: " + uri.toString());

            try {
                ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
                System.out.println("API 응답 상태 코드: " + response.getStatusCode());

                String xmlResponse = response.getBody();
                List<Map<String, String>> items = parseXmlResponse(xmlResponse);
                allItems.addAll(items);

            } catch (Exception e) {
                System.err.println("API 호출 중 오류 발생 (" + currentDealYmd + "): " + e.getMessage());
                e.printStackTrace();
            }
        }

        // 최신순으로 정렬
        allItems.sort((a, b) -> {
            String dateA = getDateString(a);
            String dateB = getDateString(b);
            return dateB.compareTo(dateA);
        });

        // 모델에 데이터 추가
        model.addAttribute("items", allItems);
        model.addAttribute("city", city);
        model.addAttribute("district", district);
        model.addAttribute("neighborhood", neighborhood);
        model.addAttribute("apartment", apartment);

        return "apt_report";
    }

    // 날짜 문자열을 생성하는 헬퍼 메서드
    private String getDateString(Map<String, String> item) {
        String year = item.get("년");
        String month = item.get("월");
        String day = item.get("일");

        if (year == null || month == null || day == null) {
            return "00000000"; // 날짜 정보가 없는 경우 가장 오래된 날짜로 처리
        }

        return year + String.format("%02d", Integer.parseInt(month)) + String.format("%02d", Integer.parseInt(day));
    }

    private List<Map<String, String>> parseXmlResponse(String xmlString) {
        List<Map<String, String>> items = new ArrayList<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(xmlString)));
            NodeList itemList = document.getElementsByTagName("item");
            for (int i = 0; i < itemList.getLength(); i++) {
                Element item = (Element) itemList.item(i);
                Map<String, String> itemMap = new HashMap<>();
                NodeList children = item.getChildNodes();
                for (int j = 0; j < children.getLength(); j++) {
                    if (children.item(j).getNodeType() == Node.ELEMENT_NODE) {
                        Element el = (Element) children.item(j);
                        String value = el.getTextContent();
                        itemMap.put(el.getNodeName(), value.isEmpty() ? null : value);
                    }
                }
                items.add(itemMap);
            }
        } catch (Exception e) {
            System.err.println("XML 파싱 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
        return items;
    }

    private String getTextContent(Element element, String tagName) {
        NodeList nodeList = element.getElementsByTagName(tagName);
        if (nodeList != null && nodeList.getLength() > 0) {
            return nodeList.item(0).getTextContent();
        }
        return "";
    }

    private void printParsedResponse(Map<String, Object> response, int indent) {
        for (Map.Entry<String, Object> entry : response.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            printIndent(indent);
            System.out.print(key + ": ");

            if (value instanceof Map) {
                System.out.println();
                printParsedResponse((Map<String, Object>) value, indent + 2);
            } else if (value instanceof List) {
                System.out.println();
                for (Object item : (List) value) {
                    if (item instanceof Map) {
                        printParsedResponse((Map<String, Object>) item, indent + 2);
                    } else {
                        printIndent(indent + 2);
                        System.out.println(item);
                    }
                }
            } else {
                System.out.println(value);
            }
        }
    }

    private void printIndent(int indent) {
        for (int i = 0; i < indent; i++) {
            System.out.print(" ");
        }
    }

    @GetMapping("/housing-weather")
    @ResponseBody
    public String getHousingWeather(@RequestParam String lawdCd) {
        String url = "https://data-api.kbland.kr/bfmstat/wthrchat/husePrcIndx";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("월간주간구분코드", "01")
                .queryParam("매매전세코드", "01")
                .queryParam("매물종별구분", "01")
                .queryParam("면적크기코드", "00")
                .queryParam("단위구분코드", "01")
                .queryParam("법정동코드", lawdCd)
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

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.getForEntity(builder.toUriString(), Map.class);
        Map<String, Object> data = response.getBody();

        String weather = "☀️"; // 기본값
        if (data != null && data.containsKey("dataBody")) {
            Map<String, Object> dataBody = (Map<String, Object>) data.get("dataBody");
            if (dataBody.containsKey("data")) {
                Map<String, Object> dataMap = (Map<String, Object>) dataBody.get("data");
                if (dataMap.containsKey("depth2")) {
                    List<Map<String, Object>> depth2 = (List<Map<String, Object>>) dataMap.get("depth2");
                    for (Map<String, Object> item : depth2) {
                        if (item.get("법정동코드").toString().equals(lawdCd)) {
                            double changeRate = Double.parseDouble(item.get("변동률").toString());
                            weather = changeRate < 0 ? "☁️" : "☀️";
                            break;
                        }
                    }
                }
            }
        }

        return weather;
    }
}