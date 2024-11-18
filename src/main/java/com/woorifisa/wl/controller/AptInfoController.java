package com.woorifisa.wl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Controller
public class AptInfoController {

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
                                  RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("city", city);
        redirectAttributes.addAttribute("district", district);
        redirectAttributes.addAttribute("neighborhood", neighborhood);
        redirectAttributes.addAttribute("apartment", apartment);

        return "redirect:/apt-report";
    }

    @GetMapping("/apt-report")
    public String showAptReport(@RequestParam(required = false) String city,
                                @RequestParam(required = false) String district,
                                @RequestParam(required = false) String neighborhood,
                                @RequestParam(required = false) String apartment,
                                Model model) {
        System.out.println("Received parameters:");
        System.out.println("City: " + city);
        System.out.println("District: " + district);
        System.out.println("Neighborhood: " + neighborhood);
        System.out.println("Apartment: " + apartment);

        // 모델에 파라미터 추가
        model.addAttribute("city", city);
        model.addAttribute("district", district);
        model.addAttribute("neighborhood", neighborhood);
        model.addAttribute("apartment", apartment);

        // apt_report 페이지 로직
        // 여기서 실제 API 호출 및 데이터 처리를 수행할 수 있습니다.
        return "apt_report";
    }
}