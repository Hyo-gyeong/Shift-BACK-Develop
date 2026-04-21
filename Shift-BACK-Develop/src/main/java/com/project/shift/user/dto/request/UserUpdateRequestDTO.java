package com.project.shift.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;

//프론트에서 user 객체 전체를 spread해서 보내지만
//실제로 수정에 사용하는 필드는 name, phone, address 3개뿐
//Jackson은 DTO에 없는 필드(userId, loginId, points)를 자동으로 무시함
@Getter
@Builder
public class UserUpdateRequestDTO {

    @NotBlank(message = "이름을 입력해야 합니다.")
    @Pattern(regexp = "^[가-힣\\s]+$", message = "이름은 한글만 사용할 수 있습니다.")
    private String name;

    @NotBlank(message = "연락처를 입력해주세요.")
    @Pattern(regexp = "^[0-9]{11}$", message = "연락처는 11자리 숫자만 입력 가능합니다.")
    private String phone;

    private String address;
}