package com.project.shift.shop.controller;

import com.project.shift.shop.service.GiftService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gifts")
public class GiftController {

    private final GiftService giftService;

    public GiftController(GiftService giftService) {
        this.giftService = giftService;
    }

    // GIFT-01 보낸 선물 조회
    @GetMapping("/sent")
    public ResponseEntity<?> getSentGifts() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(auth.getName());

        return ResponseEntity.ok(giftService.getSentGifts(userId));
    }

    // GIFT-02 받은 선물 조회
    @GetMapping("/received")
    public ResponseEntity<?> getReceivedGifts() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(auth.getName());

        return ResponseEntity.ok(giftService.getReceivedGifts(userId));
    }

    // GIFT-03 선물 상세 조회
    @GetMapping("/products/{giftId}")
    public ResponseEntity<?> getGiftDetails() {
        // 구현 예정
        return ResponseEntity.ok().build();
    }
}
