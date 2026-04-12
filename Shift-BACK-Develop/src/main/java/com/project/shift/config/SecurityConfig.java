package com.project.shift.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.project.shift.global.AuthEntryPoint;
import com.project.shift.global.filter.AuthenticationFilter;
import com.project.shift.user.service.UserDetailsServiceImpl;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Value("${cors.allowed-origins}")
    private String allowedOrigins;

    private final UserDetailsServiceImpl userDetailsService;
    private final AuthenticationFilter authenticationFilter;
    private final AuthEntryPoint exceptionHandler;
    
    // configureGlobal 메서드 삭제 — HttpSecurity DSL로 통합
 	//  @Autowired
 	//  public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
 	//      auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
 	//  }
    
     /*
      * 스프링이 시작될 때 이 메서드를 실행해 BCryptPasswordEncoder 객체를 관리소(Context)에 넣어둠
      * PasswordEncoder 빈이 Context에 있으면 자동으로 DaoAuthenticationProvider에 세팅됨
      * 사용자가 로그인할 때, 시큐리티는 알아서 내가 등록한 BCryptPasswordEncoder를 꺼내어 비밀번호를 대조
      * [configureGlobal 방식]
 		개발자가 직접 → DaoAuthenticationProvider 조립 → PasswordEncoder 명시 주입
 		
 		[DSL 방식]
 		@Bean PasswordEncoder → Spring Context 등록
 		→ Spring Security가 자동 감지
 		→ DaoAuthenticationProvider에 자동 주입
      */
     @Bean
     public PasswordEncoder passwordEncoder() {
     	// 이 빈이 자동으로 감지됨
         return new BCryptPasswordEncoder();
     }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
    		.csrf((csrf) -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // UserDetailsService와 PasswordEncoder를 DSL에서 직접 구성
            .userDetailsService(userDetailsService)
            .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
                    // preflight 요청 허용
                    .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                    .requestMatchers(HttpMethod.POST, "/auth/login", "/auth/refresh").permitAll()
                    .requestMatchers(HttpMethod.POST, "/users", "/users/check/**", "/users/find-id").permitAll()
                    .requestMatchers(HttpMethod.GET, "/products/**", "/categories/**").permitAll()
                    .requestMatchers("/ws/**", "/").permitAll() // WebSocket 연결 허용
                    .anyRequest().authenticated())
            .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling((exceptionHandling) -> exceptionHandling.authenticationEntryPoint(exceptionHandler));
        return http.build();
    }

    // CORS 설정
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(allowedOrigins.split(",")));
        configuration.addAllowedMethod("*"); // 모든 HTTP 메서드 허용
        configuration.addAllowedHeader("*"); // 모든 헤더 허용
        configuration.addExposedHeader("Set-Cookie"); // Set-Cookie 헤더 노출
        configuration.setAllowCredentials(true); // 쿠키 허용
        configuration.setMaxAge(3600L); // 1시간 동안 캐시

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
