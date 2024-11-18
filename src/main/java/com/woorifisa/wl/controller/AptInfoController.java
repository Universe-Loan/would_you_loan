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
        // 디버깅을 위한 로그 추가
        System.out.println("이건 어디? lawdCd: " + lawdCd);

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
                                @RequestParam(required = false, defaultValue = "202311") String dealYmd,
                                @RequestParam(required = false, defaultValue = "1") int pageNo,
                                @RequestParam(required = false, defaultValue = "10") int numOfRows,
                                Model model) {

        System.out.println("Received parameters:");
        System.out.println("City: " + city);
        System.out.println("District: " + district);
        System.out.println("Neighborhood: " + neighborhood);
        System.out.println("Apartment: " + apartment);
        System.out.println("디버깅 lawdCd " + lawdCd);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));

        URI uri = UriComponentsBuilder.fromHttpUrl(API_BASE_URL)
                .queryParam("serviceKey", serviceKey)
                .queryParam("LAWD_CD", lawdCd)
                .queryParam("DEAL_YMD", dealYmd)
                .queryParam("pageNo", pageNo)
                .queryParam("numOfRows", numOfRows)
                .build(true)
                .toUri();

        System.out.println("API 호출 URL: " + uri.toString());

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
            System.out.println("API 응답 상태 코드: " + response.getStatusCode());
            System.out.println("API 응답 본문: " + response.getBody());
            System.out.println("lawdCd 값: " + lawdCd);

            String xmlResponse = response.getBody();
            List<Map<String, String>> items = parseXmlResponse(xmlResponse);

            // 콘솔에 모든 item 정보 출력
            for (int i = 0; i < items.size(); i++) {
                System.out.println("Item " + (i + 1) + ":");
                for (Map.Entry<String, String> entry : items.get(i).entrySet()) {
                    System.out.println("  " + entry.getKey() + ": " + entry.getValue());
                }
                System.out.println(); // 각 item 사이에 빈 줄 추가
            }

            // 모델에 파싱된 데이터 추가
            model.addAttribute("items", items);
        } catch (Exception e) {
            System.err.println("API 호출 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }

        // 모델에 파라미터 추가
        model.addAttribute("city", city);
        model.addAttribute("district", district);
        model.addAttribute("neighborhood", neighborhood);
        model.addAttribute("apartment", apartment);

        return "apt_report";
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
                        itemMap.put(el.getNodeName(), el.getTextContent());
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
}