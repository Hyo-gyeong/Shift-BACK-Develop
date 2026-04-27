package com.project.shift.global.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.shift.global.jwt.JwtService;
import com.project.shift.user.entity.UserEntity;
import com.project.shift.user.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter { // 모든 API 요청마다 한 번만 실행됨

    private final JwtService jwtService;
    private final UserRepository userRepository;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = jwtService.extractTokenFromRequest(request);
        Long userId = null;

        if (token != null) {
            // 토큰이 유효한지 검사
            if (jwtService.isValidToken(token)) {
                userId = jwtService.extractUserIdFromValidToken(token);
            }
        }

        if (userId != null) {
            UserEntity userEntity = userRepository.findById(userId).orElse(null);

            if (userEntity != null) {
                // principal에 Long 타입으로 userId를 저장 -> @AuthenticationPrincipal Long userId로 사용
                Authentication authentication = new UsernamePasswordAuthenticationToken(userId, null, Collections.emptyList());
                SecurityContextHolder.getContext().setAuthentication(authentication);
                filterChain.doFilter(request, response);
            } else {
                // 응답 형식을 RFC 7807 ProblemDetail 로 통일
                logger.warn("탈퇴했거나 존재하지 않는 사용자입니다. userId: " + userId);
                writeProblemDetail(request, response,
                        HttpStatus.UNAUTHORIZED,
                        "인증 실패",
                        "탈퇴했거나 존재하지 않는 회원입니다.");
            }
        } else {
            // 토큰이 없거나 만료된 경우 -> 다음 필터로 넘김
            filterChain.doFilter(request, response);
        }
    }

    // ProblemDetail 직렬화 헬퍼
    // Filter는 @ExceptionHandler가 닿지 못하는 영역(서블릿 단계)에 있어 GlobalExceptionHandler와 동일한 ProblemDetail 응답을 보장하려면 이 위치에서 직접 작성.
    // 필드(status/title/detail/instance/timestamp)는 GlobalExceptionHandler.createProblemDetail과 일치.
    private void writeProblemDetail(HttpServletRequest request,
                                    HttpServletResponse response,
                                    HttpStatus status,
                                    String title,
                                    String detail) throws IOException {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
        problemDetail.setTitle(title);
        problemDetail.setInstance(java.net.URI.create(request.getRequestURI()));
        problemDetail.setProperty("timestamp", LocalDateTime.now());

        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        OBJECT_MAPPER.writeValue(response.getOutputStream(), problemDetail);
    }
}
