package com.project.shift.util;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.io.Encoders;

import javax.crypto.SecretKey;

// 일관된 키 생성을 위한 유틸리티 클래스
public class KeyGenerator {
    public static void main(String[] args) {
        // HS256 알고리즘에 적합한 비밀 키 생성
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

        String base64Key = Encoders.BASE64.encode(key.getEncoded());

        System.out.println("Generated Base64 Encoded Key: " + base64Key);
    }
}