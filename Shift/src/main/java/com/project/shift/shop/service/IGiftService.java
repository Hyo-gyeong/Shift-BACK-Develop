package com.project.shift.shop.service;

import com.project.shift.shop.dto.gift.GiftDetailResponseDTO;
import com.project.shift.shop.dto.gift.GiftListResponseDTO;

import java.util.List;

public interface IGiftService {

    // 보낸 선물 조회
    List<GiftListResponseDTO> getSentGifts(Long userId);

    // 받은 선물 조회
    List<GiftListResponseDTO> getReceivedGifts(Long userId);

    // 선물 상세 조회
    GiftDetailResponseDTO getDetailGift(Long userId, Long orderId);
}
