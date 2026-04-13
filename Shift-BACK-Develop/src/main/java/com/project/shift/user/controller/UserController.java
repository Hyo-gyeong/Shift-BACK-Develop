package com.project.shift.user.controller;

import static com.project.shift.global.security.CurrentUser.getUserIdOrNull;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.shift.shop.dto.PointHistoryResponseDTO;
import com.project.shift.shop.service.IOrderService;
import com.project.shift.user.dto.LoginIdRequestDTO;
import com.project.shift.user.dto.UserRegisterRequestDTO;
import com.project.shift.user.dto.UserResponseDTO;
import com.project.shift.user.dto.UserUpdateRequestDTO;
import com.project.shift.user.facade.WithdrawFacade;
import com.project.shift.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final IOrderService orderService;
    private final WithdrawFacade withdrawFacade;

    @PostMapping
    public ResponseEntity<?> registerUser(@RequestBody UserRegisterRequestDTO requestDTO) {
        try {
            //서버 회원가입 요청
            Long userId = userService.join(requestDTO);
            //성공 응답(201 Created)
            return new ResponseEntity<>(
            		"회원가입 성공. 할당된 사용자 ID:" + userId, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            //클라이언트 오류 응답(409 Conflict)
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            //서버 오류 응답(500 Internal Server Error)
            return new ResponseEntity<>(
            		"회원가입 중 서버 오류 발생", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 연락처 중복 확인
    @PostMapping("/check/phone")
    public ResponseEntity<?> checkPhone(@RequestBody Map<String, String> request) {
        try {
            String phone = request.get("phone");
            boolean isDuplicate = userService.isPhoneAvailable(phone);
            return ResponseEntity.ok(Map.of(
                    "available", !isDuplicate,
                    "message", isDuplicate
                    	? "이미 사용중인 연락처입니다."
                    	: "사용 가능한 연락처입니다."
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    // 아이디 중복 확인
    @PostMapping("/check")
    public ResponseEntity<?> checkLoginId(@RequestBody Map<String, String> request) {
        try {
            String loginId = request.get("loginId");
            boolean isDuplicate = userService.isLoginIdAvailable(loginId);
            return ResponseEntity.ok(Map.of(
                    "available", !isDuplicate,
                    "message", isDuplicate
                    	? "이미 사용중인 아이디입니다."
                    	: "사용 가능한 아이디입니다."
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    // 비밀번호 보안 규칙 검증
    @PostMapping("/check/pw-rule")
    public ResponseEntity<?> checkPasswordRule(@RequestBody Map<String, String> request) {
        try {
            String password = request.get("password");
            userService.validatePasswordRule(password);
            return ResponseEntity.ok(Map.of(
                    "valid", true,
                    "message", "사용 가능한 비밀번호입니다."
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "valid", false,
                    "message", e.getMessage()
            ));
        }
    }

    // 본인 정보 조회
    // 응답: UserResponseDTO (userId, loginId, name, phone, address, points)
    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getMyInfo(@AuthenticationPrincipal UserDetails userDetails) {
    	Long userId = Long.parseLong(userDetails.getUsername());
        return ResponseEntity.ok(userService.getUserInfo(userId));
    }

    // 본인 정보 수정
    @PutMapping("/info")
    public ResponseEntity<UserResponseDTO> updateMyInfo(@AuthenticationPrincipal UserDetails userDetails,
    													@RequestBody UserUpdateRequestDTO request) {
    	Long userId = Long.parseLong(userDetails.getUsername());
        return ResponseEntity.ok(userService.updateUserInfo(userId, request));
    }

    // 아이디 찾기
    @PostMapping("/find-id")
    public ResponseEntity<?> findId(@RequestBody LoginIdRequestDTO loginIdRequestDTO) {
        String loginId = userService.findId(loginIdRequestDTO);
        return ResponseEntity.ok(Map.of("loginId", loginId));
    }

    // SHOP-011 포인트 사용/적립 내역 조회
    @GetMapping("/{userId}/points")
    public ResponseEntity<PointHistoryResponseDTO> getPointHistory(@PathVariable Long userId) {
        // JWT 우선 적용 — 본인 계정만 조회 가능
        Long uid = getUserIdOrNull();
        if (uid != null && !uid.equals(userId))
            throw new AccessDeniedException("본인 계정만 조회 가능합니다.");
        return ResponseEntity.ok(orderService.getPointHistory(userId));
    }

    // 마이포인트 조회
    @GetMapping("/points")
    public ResponseEntity<?> getMyPoints(@AuthenticationPrincipal UserDetails userDetails) {
        try {
        	Long userId = Long.parseLong(userDetails.getUsername());
        	UserResponseDTO user = userService.getUserInfo(userId);
            return ResponseEntity.ok(Map.of("points", user.getPoints()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    // 비밀번호 인증
    @PostMapping("/check/password")
    public ResponseEntity<?> verifyPassword(@AuthenticationPrincipal UserDetails userDetails,
    										@RequestBody Map<String, String> request) {
    	Long userId = Long.parseLong(userDetails.getUsername());
        String password = request.get("password");
        if (password == null || password.isBlank()) {
            return ResponseEntity.badRequest()
            		.body(Map.of("valid", false, "message", "비밀번호를 입력해주세요."));
        }

        try {
            boolean isValid = userService.verifyPassword(userId, password);
            return ResponseEntity.ok(Map.of(
                    "valid", isValid,
                    "message", isValid
                    	? "비밀번호 인증에 성공했습니다."
                    	: "비밀번호가 일치하지 않습니다."
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    // 회원 탈퇴
    @DeleteMapping
    public ResponseEntity<?> withdrawUser(@AuthenticationPrincipal UserDetails userDetails) {
        try {
        	Long userId = Long.parseLong(userDetails.getUsername());
        	withdrawFacade.withdraw(userId);
        	
        	// SecurityContextHolder 처리는 Security 인프라 관심사
            // 도메인 로직(Facade)과 분리해서 Controller에서 처리
            SecurityContextHolder.clearContext();
            
            return ResponseEntity.ok(Map.of("message", "회원 탈퇴가 성공적으로 처리되었습니다."));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            		.body(Map.of("message", "회원 탈퇴 중 서버 오류 발생"));
        }
    }
}