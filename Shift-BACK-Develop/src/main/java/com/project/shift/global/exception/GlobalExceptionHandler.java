package com.project.shift.global.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 잘못된 요청 파라미터, 비즈니스 규칙 위반 (유효성 검증 실패 등)
    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleIllegalArgument(IllegalArgumentException e) {
        log.warn("[GLOBAL] IllegalArgumentException: {}", e.getMessage());
        return createProblemDetail(HttpStatus.BAD_REQUEST, "잘못된 요청", e.getMessage());
    }

    // 현재 상태에서 수행 불가 (예: 배송 중인 상품이 있어 탈퇴 불가)
    @ExceptionHandler(IllegalStateException.class)
    public ProblemDetail handleIllegalState(IllegalStateException e) {
        log.warn("[GLOBAL] IllegalStateException: {}", e.getMessage());
        return createProblemDetail(HttpStatus.BAD_REQUEST, "처리 불가", e.getMessage());
    }

    // 권한 없음 (본인 외 리소스 접근 시도 등)
    @ExceptionHandler(AccessDeniedException.class)
    public ProblemDetail handleAccessDenied(AccessDeniedException e) {
        log.warn("[GLOBAL] AccessDeniedException: {}", e.getMessage());
        return createProblemDetail(HttpStatus.FORBIDDEN, "접근 거부", e.getMessage());
    }

    // 인증 실패 (잘못된 비밀번호, 유효하지 않은 리프레시 토큰 등)
    @ExceptionHandler(BadCredentialsException.class)
    public ProblemDetail handleBadCredentials(BadCredentialsException e) {
        log.warn("[GLOBAL] BadCredentialsException: {}", e.getMessage());
        return createProblemDetail(HttpStatus.UNAUTHORIZED, "인증 실패", e.getMessage());
    }

    // 위 핸들러에서 잡지 못한 모든 예외 (마지막 안전망)
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleUnexpected(Exception e) {
        log.error("[GLOBAL] 예기치 못한 서버 오류 발생", e);
        return createProblemDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "서버 오류",
                "서버에서 알 수 없는 오류가 발생했습니다."
        );
    }

    // ProblemDetail 생성 중복 제거용 헬퍼
    // RFC 7807 표준 필드 + 공통 custom property(timestamp) 부여
    private ProblemDetail createProblemDetail(HttpStatus status, String title, String detail) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
        problemDetail.setTitle(title);
        problemDetail.setProperty("timestamp", LocalDateTime.now());
        return problemDetail;
    }
}
