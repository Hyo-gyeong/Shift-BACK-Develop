package com.project.shift.global.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

// ================================================================
// [임시 복구] 2026-04-15
//   cd9d7c3 커밋에서 이 파일이 삭제됐으나, OrderService 가 여전히
//   getUserIdOrNull() 을 사용하고 있어 컴파일 에러 발생.
//
//   OrderService 리팩토링(컨트롤러에서 @AuthenticationPrincipal 로
//   userId 를 파라미터로 넘기도록 변경) 이 끝나면 다시 삭제할 것.
// ================================================================
public final class CurrentUser {
    private CurrentUser() {}

    public static Long getUserIdOrNull() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return null;
        Object principal = auth.getPrincipal();
        if (principal instanceof Long) return (Long) principal;
        if (principal instanceof String s && s.matches("\\d+")) return Long.parseLong(s);
        return null;
    }
}
