package com.woorifisa.wl.controller;

import com.woorifisa.wl.model.dto.UserUpdateDto;
import com.woorifisa.wl.model.entity.User;
import com.woorifisa.wl.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/my-info-page")
    public User getUserInfo(HttpSession session) {
        // 세션에서 user_id 가져오기
        Long userId = (Long) session.getAttribute("user_id");

        if (userId == null) {
            throw new IllegalStateException("세션에 user_id가 존재하지 않습니다.");
        }

        // userId로 사용자 정보 조회
        return userService.findById(userId);
    }
}
