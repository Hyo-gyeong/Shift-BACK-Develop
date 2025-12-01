package com.project.shift.shop.repository;

import com.project.shift.shop.entity.Order;

import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;


import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findBySenderIdOrderByOrderDateDesc(Long senderId);
    Optional<Order> findByOrderIdAndSenderId(Long orderId, Long senderId);


    @Modifying
    @Transactional
    @Query("""
        UPDATE Order o 
           SET o.orderStatus = :status,
               o.remainPoints = :remainPoints
         WHERE o.orderId = :orderId
    """)
    void updateOrderStatusAndPoints(
            @Param("orderId") Long orderId,
            @Param("status") String status,
            @Param("remainPoints") Integer remainPoints
    );

    // 받은 선물 목록 조회
    List<Order> findByReceiverIdAndOrderStatusNotOrderByOrderDateDesc(Long receiverId, String status);
}
