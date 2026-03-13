package com.project.shift.shop.repository;

import com.project.shift.shop.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    // 주문 한 건에 대한 배송 정보
    Optional<Delivery> findByOrder_OrderId(Long orderId);

    // 탈퇴하려는 사용자가 sender 이면서 아직 완료/취소되지 않은 배송이 있는지 확인
    boolean existsByOrder_SenderIdAndDeliveryStatusIn(Long senderId, List<String> statuses);
}
