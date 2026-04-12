package com.project.shift.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponseDTO {
    private Long userId;
    private String loginId;
    private String name;
    private String phone;
    private String address;
    private Integer points;
}