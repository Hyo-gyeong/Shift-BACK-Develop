package com.project.shift.chat.dto;

import com.project.shift.user.entity.UserEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    private long userId;
    private String loginId;
    private String password;
    private String name;
    private String phone;
    private String address;
    private int points;
    private String refreshToken;
    private String adminFlag; // DEFAULT 'N', 'Y' 또는 'N'

    // Entity -> DTO 변환
    public static UserDTO toDto(UserEntity entity) {
        return UserDTO.builder()
                .userId(entity.getUserId())
                .loginId(entity.getLoginId())
                .password(entity.getPassword())
                .name(entity.getName())
                .phone(entity.getPhone())
                .address(entity.getAddress())
                .points(entity.getPoints())
                .refreshToken(entity.getRefreshToken())
                .adminFlag(entity.getAdminFlag())
                .build(); // 생성자 호출
    }

}
