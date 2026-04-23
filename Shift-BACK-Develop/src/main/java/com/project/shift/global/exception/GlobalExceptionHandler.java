package com.project.shift.global.exception;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

// [리팩토링 2026-04-19] GlobalExceptionHandler 확장
// 추가 사항:
//   1) BusinessException 핸들러 도입 — 도메인 예외 마다 자기 고유의 status/title 을 가져오므로
//      이 핸들러 하나로 UserNotFoundException(404) / InvalidPasswordException(401) /
//      DuplicatePhoneException(409) 등을 의미에 맞는 HTTP 상태코드로 내려줄 수 있음.
//      이전에는 모두 IllegalArgumentException → 400 으로 뭉쳐 내려가 클라이언트 분기가 어려웠음.
//   2) MethodArgumentNotValidException 핸들러 도입 — @Valid 가 걸린 DTO 의 Bean Validation 실패를
//      필드별 에러 맵 ("errors" custom property) 으로 묶어 400 응답.
//      → 기존 수동 검증(idValidate/passwordValidate) 을 Bean Validation 으로 위임 가능해짐.
//   3) IllegalStateException 의 상태코드를 409 CONFLICT 로 조정.
//      이전에는 400 으로 처리했으나 "상태상 수행 불가"(예: 배송 중인 상품이 있어 탈퇴 불가) 는
//      RESTful 관점에서 충돌 상황이므로 409 가 적절.
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // [리팩토링 2026-04-19] 도메인 예외 공통 처리
    // BusinessException 서브클래스(UserNotFoundException 등)는 자기 고유의 status/title 을 갖고 있음.
    // 이 핸들러가 해당 값을 그대로 ProblemDetail 에 실어 내려줌.
    @ExceptionHandler(BusinessException.class)
    public ProblemDetail handleBusiness(BusinessException e) {
        log.warn("[GLOBAL] {}: {}", e.getClass().getSimpleName(), e.getMessage());
        return createProblemDetail(e.getStatus(), e.getTitle(), e.getMessage());
    }

    // @RequestBody @Valid 실패는 MethodArgumentNotValidException
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        Map<String, String> errors = new LinkedHashMap<>();
        for (FieldError fe : fieldErrors) {
            errors.put(fe.getField(), fe.getDefaultMessage());
        }
        log.warn("[GLOBAL] Validation failed: {}", errors);

        ProblemDetail pd = createProblemDetail(
                HttpStatus.BAD_REQUEST,
                "입력값 오류",
                "요청 값이 유효성 검증에 실패했습니다."
        );
        pd.setProperty("errors", errors);
        return pd;
    }

    // 잘못된 요청 파라미터 (포맷/타입 위반 등 순수 형식 오류)
    // 도메인 규칙 위반은 BusinessException 을 쓰도록 점진 이관하지만,
    // 외부 라이브러리나 기타 루틴이 여전히 IllegalArgumentException 을 던질 수 있어 안전망으로 유지.
    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleIllegalArgument(IllegalArgumentException e) {
        log.warn("[GLOBAL] IllegalArgumentException: {}", e.getMessage());
        return createProblemDetail(HttpStatus.BAD_REQUEST, "잘못된 요청", e.getMessage());
    }

    // [리팩토링 2026-04-19] IllegalStateException 의 상태코드 400 -> 409 CONFLICT
    // 이전: createProblemDetail(HttpStatus.BAD_REQUEST, "처리 불가", ...)
    // 개선: "현재 상태로는 수행 불가"(예: 배송 중인 상품 존재로 탈퇴 불가) 는 RESTful 관점에서
    //       전형적인 충돌 상황이므로 409 가 의미상 정확.
    @ExceptionHandler(IllegalStateException.class)
    public ProblemDetail handleIllegalState(IllegalStateException e) {
        log.warn("[GLOBAL] IllegalStateException: {}", e.getMessage());
        return createProblemDetail(HttpStatus.CONFLICT, "처리 불가", e.getMessage());
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
    
	 // @Validated 적용 시 파라미터 레벨 검증 실패 처리
	 // @Validated + 파라미터 직접 검증 실패는 ConstraintViolationException으로 분리됨
	 @ExceptionHandler(ConstraintViolationException.class)
	 public ProblemDetail handleConstraintViolation(ConstraintViolationException e) {
	     Map<String, String> errors = new LinkedHashMap<>();
	     e.getConstraintViolations().forEach(cv -> {
	         String field = cv.getPropertyPath().toString();
	         errors.put(field, cv.getMessage());
	     });
	     log.warn("[GLOBAL] ConstraintViolation: {}", errors);
	
	     ProblemDetail pd = createProblemDetail(
	             HttpStatus.BAD_REQUEST,
	             "입력값 오류",
	             "요청 값이 유효성 검증에 실패했습니다."
	     );
	     pd.setProperty("errors", errors);
	     return pd;
	 }
	 
	// required = true인 @CookieValue 누락 시
	// ex) refreshToken 쿠키 없이 /auth/refresh 요청
	@ExceptionHandler(MissingRequestCookieException.class)
	public ProblemDetail handleMissingCookie(MissingRequestCookieException e) {
	    log.warn("[GLOBAL] MissingRequestCookieException: {}", e.getMessage());
	    return createProblemDetail(
	            HttpStatus.BAD_REQUEST,
	            "잘못된 요청",
	            "'" + e.getCookieName() + "' 쿠키가 존재하지 않습니다."
	    );
	}

	// required = true인 @RequestHeader 누락 시
	// ex) Authorization 헤더 없이 /auth/refresh 요청
	@ExceptionHandler(MissingRequestHeaderException.class)
	public ProblemDetail handleMissingHeader(MissingRequestHeaderException e) {
	    log.warn("[GLOBAL] MissingRequestHeaderException: {}", e.getMessage());
	    return createProblemDetail(
	            HttpStatus.BAD_REQUEST,
	            "잘못된 요청",
	            "'" + e.getHeaderName() + "' 헤더가 존재하지 않습니다."
	    );
	}
}
