package com.project.shift.user.dto;

import lombok.Builder;
import lombok.Getter;

//프론트에서 user 객체 전체를 spread해서 보내지만
//실제로 수정에 사용하는 필드는 name, phone, address 3개뿐
//Jackson은 DTO에 없는 필드(userId, loginId, points)를 자동으로 무시함
@Getter
@Builder
public class UserUpdateRequestDTO {
    private String name;
    private String phone;
    private String address;
}