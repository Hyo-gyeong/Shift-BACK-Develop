package com.project.shift.shop.service;

import com.project.shift.shop.dto.gift.GiftListResponseDTO;

import java.util.List;

public interface IGiftService {

    // 받은 선물 조회
    List<GiftListResponseDTO> getReceivedGifts(Long userId);
}
