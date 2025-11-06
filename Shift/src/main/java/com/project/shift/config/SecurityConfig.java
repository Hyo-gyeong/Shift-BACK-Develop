package com.project.shift.config;

import com.project.shift.global.filter.AuthenticationFilter;
import com.project.shift.user.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    // 변경사항
    // SecurityFilterChain에서 인증 필터를 추가
    // CSRF 비활성화 및 세션 관리를 Stateless로 설정
    // 특정 엔드포인트에 대한 접근 권한 설정
    // 모든 요청에 대해 인증 필요
    // AuthenticationFilter를 UsernamePasswordAuthenticationFilter 앞에 추가
    // BCryptPasswordEncoder를 사용하여 비밀번호 암호화
    // AuthenticationManager를 빈으로 등록하여 다른 컴포넌트에서 주입 가능하도록 설정
    // Security 설정을 통해 애플리케이션의 보안 요구사항을 충족
    // 전반적인 보안 구성을 담당하는 클래스
    // Spring Security와 관련된 설정을 포함
    // 사용자 인증 및 권한 부여 설정
    // JWT 기반 인증 필터 통합
    // RESTful API 보안 설정
    // 토큰 기반 인증 메커니즘 구현
    // 사용자 세부 정보 서비스와 암호 인코더 설정

    private final UserDetailsServiceImpl userDetailsService;
    private final AuthenticationFilter authenticationFilter;

    public SecurityConfig(UserDetailsServiceImpl userDetailsService, AuthenticationFilter authenticationFilter) {
        this.userDetailsService = userDetailsService;
        this.authenticationFilter = authenticationFilter;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf((csrf) -> csrf.disable())
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/logout").permitAll()
                        .requestMatchers(HttpMethod.POST, "/users").permitAll()
                        .requestMatchers(HttpMethod.POST, "/users/refresh").permitAll() // 토큰 재발급
                        .requestMatchers(HttpMethod.GET, "/").permitAll()
                        .anyRequest().permitAll()) // 배포 전까지 모두 허용
                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
