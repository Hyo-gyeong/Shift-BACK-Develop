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

    // SHOP-016 : sender 제외한 receiver 조회
    @Override
    public Long findReceiverInChatroom(Long chatroomId, Long senderId) {

        String sql =
            "SELECT cu.user_id " +
            "FROM chatroom_users cu " +
            "WHERE cu.chatroom_id = :chatroomId " +
            "AND cu.user_id <> :senderId";

        List<Number> result = em.createNativeQuery(sql)
                .setParameter("chatroomId", chatroomId)
                .setParameter("senderId", senderId)
                .getResultList();

        if (result.isEmpty()) return null;

        return result.get(0).longValue();
    }

    // SHOP-017 : 주문 상태 변경 (결제 완료)
    @Override
    public void updateOrderStatus(Long orderId, String status) {
        String jpql =
                "UPDATE Order o SET o.orderStatus = :status WHERE o.orderId = :orderId";

        em.createQuery(jpql)
                .setParameter("status", status)
                .setParameter("orderId", orderId)
                .executeUpdate();
    }
}