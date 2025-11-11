package com.project.shift.shop.dao;

import com.project.shift.shop.entity.Order;
import com.project.shift.shop.repository.OrderRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public class OrderDAO implements IOrderDAO {

    private final OrderRepository orderRepository;

    public OrderDAO(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Order save(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public Optional<Order> findById(Long orderId) {
        return orderRepository.findById(orderId);
    }
}
