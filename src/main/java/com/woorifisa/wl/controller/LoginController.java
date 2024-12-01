package com.woorifisa.wl.controller;

import com.woorifisa.wl.model.entity.User;
import com.woorifisa.wl.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping("/login-success")
    public String loginSuccess(HttpSession session) {
        Long userId = (Long) session.getAttribute("user_id");
        if (userId != null) {
            User user = userService.findById(userId);
            if (!user.isAddInfo()) {
                return "redirect:/login-extra";
            }
        }
        return "redirect:/";
    }
}