package com.project.shift.shop.dao;

import com.project.shift.shop.entity.Order;
import java.util.Optional;
import java.util.List;

public interface IOrderDAO {

    Order save(Order order);

    Optional<Order> findById(Long orderId);

    List<Order> findBySenderId(Long senderId);

    // SHOP-016 : 채팅방에서 senderId를 제외한 상대(receiver) 조회
    
}
