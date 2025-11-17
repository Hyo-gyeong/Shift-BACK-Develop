package com.project.shift.auth.controller;

import com.project.shift.auth.dto.LoginRequestDTO;
import com.project.shift.auth.dto.LoginResponseDTO;
import com.project.shift.auth.dto.RefreshTokenRequestDTO;
import com.project.shift.auth.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final String HEADER = "Authorization";
    private final String TOKEN_HEADER = "Bearer ";

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // 로그인 기능
    @PostMapping("/login")
    public ResponseEntity<?> userLogin(@RequestBody LoginRequestDTO loginDto) {
        log.info("[AUTH] 로그인 시도 User ID: {}", loginDto.loginId());
        idValidate(loginDto.loginId());
        passwordValidate(loginDto.password());
        LoginResponseDTO response = authService.login(loginDto);
        log.info("[AUTH] 로그인 성공 User ID: {}", loginDto.loginId());
        return ResponseEntity.ok(response);
    }

    // 비밀번호 기본 검증
    private static void passwordValidate(String password) {
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("[SYSTEM] 비밀번호가 입력되지 않았습니다.");
        }
        if (password.length() > 10) {
            throw new IllegalArgumentException("[SYSTEM] 비밀번호의 길이가 너무 깁니다.");
        }
    }

    // 아이디 기본 검증
    private static void idValidate(String loginId) {
        if (loginId == null || loginId.isBlank()) {
            throw new IllegalArgumentException("[SYSTEM] 값이 입력되지 않았습니다.");
        }
        if (loginId.length() > 20) {
            throw new IllegalArgumentException("[SYSTEM] 아이디 길이가 너무 깁니다.");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        authService.logout();
        return ResponseEntity.ok(Map.of("message", "로그아웃이 정상적으로 처리되었습니다."));
    }

    // Access 토큰 재발급 기능
    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDTO> refreshToken(@RequestHeader(HEADER) String authorizationHeader,
                                                         @RequestBody RefreshTokenRequestDTO refreshDto) {
        String accessToken = authorizationHeader.replace(TOKEN_HEADER, "");
        String refreshToken = refreshDto.refreshToken();

        LoginResponseDTO response = authService.refresh(accessToken, refreshToken);

        return ResponseEntity.ok(response);
    }
}
