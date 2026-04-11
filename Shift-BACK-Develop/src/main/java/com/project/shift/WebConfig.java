package com.project.shift;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//CORS 설정은 SecurityConfig의 CorsConfigurationSource 빈으로 일원화
//Spring Security 환경에서는 Security Filter Chain이 먼저 동작하므로
//WebMvcConfigurer의 addCorsMappings는 사실상 무시됨
@Configuration
public class WebConfig implements WebMvcConfigurer {
	// 추후 View Resolver, MessageConverter 등 MVC 설정이 필요할 때 사용
}
