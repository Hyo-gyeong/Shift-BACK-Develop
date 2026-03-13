package com.project.shift.shop.dao;

import com.project.shift.shop.entity.Order;
import java.util.Optional;
import java.util.List;

public interface IOrderDAO {

    Order save(Order order);

    Optional<Order> findById(Long orderId);

    List<Order> findBySenderId(Long senderId);

    // SHOP-016 : 채팅방 내 sender 제외한 상대방 조회
    Long findReceiverInChatroom(Long chatroomId, Long senderId);

    // SHOP-017 : 주문 상태 업데이트 (결제 완료)
    void updateOrderStatus(Long orderId, String status);

    // 받은 선물 조회
    List<Order> findReceivedGifts(Long receiverId);
}
