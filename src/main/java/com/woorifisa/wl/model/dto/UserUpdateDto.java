package com.woorifisa.wl.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserUpdateDto {
    private Long houseNum;
    private Long annualIncome;
    private String job;
    private String jobDate;
    private boolean addInfo;
}