package com.woorifisa.wl.controller;

import com.woorifisa.wl.model.dto.UserUpdateDto;
import com.woorifisa.wl.model.entity.User;
import com.woorifisa.wl.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/update-info")
    public ResponseEntity<?> updateUserInfo(@RequestBody UserUpdateDto userUpdateDto, HttpSession session) {
        try {
            Long userId = (Long) session.getAttribute("user_id");
            if (userId == null) {
                return ResponseEntity.status(401).body("User not logged in");
            }

            User updatedUser = userService.updateUserInfo(userId, userUpdateDto);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error updating user info: " + e.getMessage());
        }
    }
}
