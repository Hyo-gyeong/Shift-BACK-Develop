package com.project.shift.shop.repository;

import com.project.shift.shop.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    // 주문 한 건에 대한 배송 정보
    Optional<Delivery> findByOrder_OrderId(Long orderId);
    
    // SHOP-014 배송 조회
    
    // SHOP-015 배송 상태 변경
    
}
