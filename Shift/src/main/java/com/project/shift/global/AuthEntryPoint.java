package com.project.shift.global;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

// 토큰 인증 실패시 401 에러를 JSON 형식으로 반환
@Slf4j
@Component
public class AuthEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.warn("[AUTH_FAIL] 인증 실패 (401) Path: {}, Error: {}", request.getServletPath(), authException.getMessage());

        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Map<String, Object> body = new HashMap<>();
        body.put("status", 401); // 에러 번호
        body.put("error", "Unauthorized"); // 에러 종류
        body.put("message", authException.getMessage());
        body.put("path", request.getServletPath()); // 에러 경로

        ObjectMapper mapper = new ObjectMapper(); // Map을 Json으로 바꿔주는 ObjectMapper 사용
        mapper.writeValue(response.getOutputStream(), body);
    }
}
