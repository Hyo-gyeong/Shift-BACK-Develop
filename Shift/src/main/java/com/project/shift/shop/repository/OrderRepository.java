package com.project.shift.shop.repository;

import com.project.shift.shop.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findBySenderIdOrderByOrderDateDesc(Long senderId);
}
