package com.project.shift.shop.service;

import com.project.shift.shop.dao.IOrderDAO;
import com.project.shift.shop.dto.OrderDTO;
import com.project.shift.shop.dto.OrderItemDTO;
import com.project.shift.shop.entity.Order;
import com.project.shift.shop.entity.OrderItem;
import com.project.shift.product.entity.Product;
import com.project.shift.product.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OrderService implements IOrderService {

    private final IOrderDAO orderDAO;
    private final ProductRepository productRepository;

    public OrderService(IOrderDAO orderDAO,
                        ProductRepository productRepository) {
        this.orderDAO = orderDAO;
        this.productRepository = productRepository;
    }

    @Override
    public OrderDTO createOrder(OrderDTO orderDTO) {
        Order order = new Order();
        order.setSenderId(orderDTO.getSenderId());
        order.setReceiverId(orderDTO.getReceiverId());
        order.setOrderDate(LocalDateTime.now());
        order.setOrderStatus("P"); // DDL에서 P/C로 관리

        int totalPrice = 0;

        // items에 대해서 DB에서 가격 가져오기
        for (OrderItemDTO itemDTO : orderDTO.getItems()) {
            Product product = productRepository.findById(itemDTO.getProductId())
                    .orElseThrow(() ->
                            new IllegalArgumentException("존재하지 않는 상품입니다. product_id=" + itemDTO.getProductId()));

            int unitPrice = product.getPrice(); // DB 가격
            int linePrice = unitPrice * itemDTO.getQuantity();
            totalPrice += linePrice;

            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(itemDTO.getProductId());
            orderItem.setQuantity(itemDTO.getQuantity());
            orderItem.setItemPrice(unitPrice);  // 주문상세에도 저장
            order.addItem(orderItem);
        }

        order.setTotalPrice(totalPrice);

        Order saved = orderDAO.save(order);

        // 응답 DTO
        OrderDTO resp = new OrderDTO();
        resp.setOrderId(saved.getOrderId());
        resp.setSenderId(saved.getSenderId());
        resp.setReceiverId(saved.getReceiverId());
        resp.setTotalPrice(saved.getTotalPrice());
        resp.setOrderDate(saved.getOrderDate());
        resp.setOrderStatus(saved.getOrderStatus());
        resp.setResult(true);
        resp.setItems(orderDTO.getItems()); // 요청에서 온 아이템 구조는 그대로 돌려줌
        return resp;
    }
}
