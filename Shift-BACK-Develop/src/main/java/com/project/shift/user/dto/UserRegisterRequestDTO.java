package com.project.shift.user.dto;

import lombok.Getter;

@Getter
public class UserRegisterRequestDTO {
    private String loginId;
    private String password;
    private String name;
    private String phone;
    private String address;
    private Boolean termsAgreed;
}