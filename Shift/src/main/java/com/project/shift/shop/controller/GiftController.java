package com.project.shift.shop.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gifts")
public class GiftController {

    // GIFT-01 보낸 선물 조회
    @GetMapping("/sent/products")
    public ResponseEntity<?> getSentGifts() {
        // 구현 예정
        return ResponseEntity.ok().build();
    }

    // GIFT-02 보낸 금액권 조회
    @GetMapping("/sent/points")
    public ResponseEntity<?> getSentPointGifts() {
        // 구현 예정
        return ResponseEntity.ok().build();
    }

    // GIFT-03 받은 선물 조회
    @GetMapping("/received/products")
    public ResponseEntity<?> getReceivedGifts() {
        // 구현 예정
        return ResponseEntity.ok().build();
    }

    // GIFT-04 받은 금액권 조회
    @GetMapping("/received/points")
    public ResponseEntity<?> getReceivedPointGifts() {
        // 구현 예정
        return ResponseEntity.ok().build();
    }
}
