package com.project.shift.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserDTO {
    private String loginId;
    private String password;
    private String name;
    private String phone;
    private String address;
    private Boolean termsAgreed;
}
