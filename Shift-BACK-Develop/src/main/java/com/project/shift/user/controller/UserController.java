package com.project.shift.user.controller;

import java.util.Map;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.shift.shop.dto.PointHistoryResponseDTO;
import com.project.shift.shop.service.IOrderService;
import com.project.shift.user.dto.request.LoginIdRequestDTO;
import com.project.shift.user.dto.request.UserRegisterRequestDTO;
import com.project.shift.user.dto.request.UserUpdateRequestDTO;
import com.project.shift.user.dto.response.UserResponseDTO;
import com.project.shift.user.facade.WithdrawFacade;
import com.project.shift.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Validated  // @RequestBody 없이 파라미터에 직접 쓰는 @Valid 계열 어노테이션 활성화
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final IOrderService orderService;
    private final WithdrawFacade withdrawFacade;

    // 회원 가입
    @PostMapping
    public ResponseEntity<String> registerUser(@RequestBody @Valid UserRegisterRequestDTO requestDTO) {
        Long userId = userService.join(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("회원가입 성공. 할당된 사용자 ID:" + userId);
    }

    // 연락처 중복 확인
    // Map 대신 record로 받아 @Valid 적용 — Service의 null/형식 검사 제거 가능
    @PostMapping("/check/phone")
    public ResponseEntity<Map<String, Object>> checkPhone(
            @RequestBody @Valid PhoneRequest request) {
        boolean isDuplicate = userService.isPhoneAvailable(request.phone());
        return ResponseEntity.ok(Map.of(
                "available", !isDuplicate,
                "message", isDuplicate
                        ? "이미 사용중인 연락처입니다."
                        : "사용 가능한 연락처입니다."
        ));
    }

    // 아이디 중복 확인
    @PostMapping("/check")
    public ResponseEntity<Map<String, Object>> checkLoginId(
            @RequestBody @Valid LoginIdCheckRequest request) {
        boolean isDuplicate = userService.isLoginIdAvailable(request.loginId());
        return ResponseEntity.ok(Map.of(
                "available", !isDuplicate,
                "message", isDuplicate
                        ? "이미 사용중인 아이디입니다."
                        : "사용 가능한 아이디입니다."
        ));
    }

    // 비밀번호 보안 규칙 검증
    @PostMapping("/check/pw-rule")
    public ResponseEntity<Map<String, Object>> checkPasswordRule(
            @RequestBody @Valid PasswordRuleRequest request) {
        return ResponseEntity.ok(Map.of(
                "valid", true,
                "message", "사용 가능한 비밀번호입니다."
        ));
    }

    // 본인 정보 조회
    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getMyInfo(@AuthenticationPrincipal Long userId) {
        return ResponseEntity.ok(userService.getUserInfo(userId));
    }

    // 본인 정보 수정
    @PutMapping("/info")
    public ResponseEntity<UserResponseDTO> updateMyInfo(
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid UserUpdateRequestDTO request) {
        return ResponseEntity.ok(userService.updateUserInfo(userId, request));
    }

    // 아이디 찾기
    @PostMapping("/find-id")
    public ResponseEntity<?> findId(@RequestBody @Valid LoginIdRequestDTO loginIdRequestDTO) {
        String loginId = userService.findId(loginIdRequestDTO);
        return ResponseEntity.ok(Map.of("loginId", loginId));
    }

    // 포인트 사용/적립 내역 조회
    @GetMapping("/points/history")
    public ResponseEntity<PointHistoryResponseDTO> getPointHistory(
            @AuthenticationPrincipal Long userId) {
        return ResponseEntity.ok(orderService.getPointHistory(userId));
    }

    // 마이포인트 조회
    @GetMapping("/points")
    public ResponseEntity<Map<String, Object>> getMyPoints(@AuthenticationPrincipal Long userId) {
        UserResponseDTO user = userService.getUserInfo(userId);
        return ResponseEntity.ok(Map.of("points", user.getPoints()));
    }

    // 비밀번호 인증
    // [리팩토링] Map<String, String> → PasswordVerifyRequest record로 교체
    @PostMapping("/check/password")
    public ResponseEntity<Map<String, Object>> verifyPassword(
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid PasswordVerifyRequest request) {
        boolean isValid = userService.verifyPassword(userId, request.password());
        return ResponseEntity.ok(Map.of(
                "valid", isValid,
                "message", isValid
                        ? "비밀번호 인증에 성공했습니다."
                        : "비밀번호가 일치하지 않습니다."
        ));
    }

    // 회원 탈퇴
    @DeleteMapping
    public ResponseEntity<Map<String, Object>> withdrawUser(@AuthenticationPrincipal Long userId) {
        withdrawFacade.withdraw(userId);
        return ResponseEntity.ok(Map.of("message", "회원 탈퇴가 성공적으로 처리되었습니다."));
    }

    // ── 인라인 Request record ──────────────────────────────────────────
    // 단일 필드 요청처럼 별도 파일을 만들기엔 가벼운 경우 Controller 내부에 record로 선언

    record PhoneRequest(
        @NotBlank(message = "연락처를 입력해주세요.")
        @Pattern(regexp = "^[0-9]{11}$", message = "연락처는 11자리 숫자만 입력 가능합니다.")
        String phone
    ) {}

    record LoginIdCheckRequest(
        @NotBlank(message = "아이디를 입력해주세요.")
        @Pattern(regexp = "^[A-Za-z0-9]{4,20}$", message = "아이디는 4~20자의 영문, 숫자만 사용할 수 있습니다.")
        String loginId
    ) {}

    record PasswordRuleRequest(
        @NotBlank(message = "비밀번호를 입력해야 합니다.")
        String password
    ) {}

    record PasswordVerifyRequest(
        @NotBlank(message = "비밀번호를 입력해주세요.")
        String password
    ) {}
}