package com.project.shift.auth.controller;

import com.project.shift.auth.dto.request.LoginRequestDTO;
import com.project.shift.auth.dto.response.LoginResponseDTO;
import com.project.shift.auth.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    // [리팩토링 2026-04-19] @Valid + 수동 검증 제거
    @PostMapping("/login")
    public ResponseEntity<?> userLogin(@Valid @RequestBody LoginRequestDTO request, HttpServletResponse response) {
        log.info("[AUTH] 로그인 시도 User ID: {}", request.loginId());

        LoginResponseDTO tokens = authService.login(request);
        log.info("[AUTH] 로그인 성공 User ID: {}", request.loginId());

        ResponseCookie cookie = createRefreshTokenCookie(tokens.refreshToken());
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok(Map.of("accessToken", tokens.accessToken()));
    }

    // [리팩토링 2026-04-19] 수동 검증 메서드 제거

    // [리팩토링 2026-04-19] logout() 에 @AuthenticationPrincipal Long userId 주입
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@AuthenticationPrincipal Long userId) {
        authService.logout(userId);

        // 로그아웃 시 쿠키 삭제
        ResponseCookie deleteCookie = ResponseCookie.from("refreshToken", "")
                .path("/auth/refresh")
                .maxAge(0) // 즉시 만료
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, deleteCookie.toString())
                .header("Clear-Site-Data", "\"cookies\", \"storage\", \"cache\"") // 좀비 쿠키 방지를 위한 추가 헤더
                .body(Map.of("message", "로그아웃이 정상적으로 처리되었습니다."));
    }

    // Access 토큰 재발급 기능
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(
            @RequestHeader(value = HEADER) String authorizationHeader,
            @CookieValue(name = "refreshToken") String refreshToken) {

        String accessToken = authorizationHeader.replace(TOKEN_HEADER, "");
        LoginResponseDTO tokens = authService.refresh(accessToken, refreshToken);

        ResponseCookie newRefreshCookie = createRefreshTokenCookie(tokens.refreshToken());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, newRefreshCookie.toString())
                .body(Map.of("accessToken", tokens.accessToken()));
    }

    // 리프레시 토큰 쿠키 생성
    private ResponseCookie createRefreshTokenCookie(String refreshToken) {
        return ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true) // JavaScript에서 접근 불가
                .secure(false)
                .path("/auth/refresh") // 특정 경로에서만 전송
                .maxAge(7 * 24 * 60 * 60) // 7일
                .sameSite("Lax") // CSRF 방어
                .build();
    }
}
