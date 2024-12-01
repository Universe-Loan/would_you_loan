package com.woorifisa.wl.service;

import com.woorifisa.wl.model.dto.UserUpdateDto;
import com.woorifisa.wl.model.entity.User;
import com.woorifisa.wl.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User updateUserInfo(Long userId, UserUpdateDto userUpdateDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setHouseNum(userUpdateDto.getHouseNum());
        user.setAnnualIncome(userUpdateDto.getAnnualIncome());
        user.setJob(userUpdateDto.getJob());
        // YYYY-MM 형식의 String을 LocalDateTime으로 변환
        user.setJobDate(LocalDateTime.parse(userUpdateDto.getJobDate() + "T00:00:00"));
        user.setAddInfo(userUpdateDto.isAddInfo());

        return userRepository.save(user);
    }

    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
    }
}
