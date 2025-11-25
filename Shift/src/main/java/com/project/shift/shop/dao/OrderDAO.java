package com.project.shift.shop.dao;

import com.project.shift.shop.entity.Order;
import com.project.shift.shop.repository.OrderRepository;

import jakarta.persistence.EntityManager;

import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;


@Repository
public class OrderDAO implements IOrderDAO {

    private final OrderRepository orderRepository;
    private final EntityManager em;

    public OrderDAO(OrderRepository orderRepository, EntityManager em) {
        this.orderRepository = orderRepository;
        this.em = em;
    }

    @Override
    public Order save(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public Optional<Order> findById(Long orderId) {
        return orderRepository.findById(orderId);
    }

    @Override
    public List<Order> findBySenderId(Long senderId) {
        return orderRepository.findBySenderIdOrderByOrderDateDesc(senderId);
    }

    // SHOP-016: 채팅방에서 senderId 제외한 receiverId 조회
    

}