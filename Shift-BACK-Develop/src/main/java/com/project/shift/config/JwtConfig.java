package com.project.shift.config;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Configuration
public class JwtConfig {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.algorithm}")
    private String algorithm;

    @Bean
    public SecretKey jwtSecretKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    @Bean
    public SignatureAlgorithm jwtSignatureAlgorithm() {
        try {
            return SignatureAlgorithm.valueOf(algorithm);
        } catch (IllegalArgumentException e) {
            // 기본값으로 HS256 사용
            return SignatureAlgorithm.HS256;
        }
    }
}
