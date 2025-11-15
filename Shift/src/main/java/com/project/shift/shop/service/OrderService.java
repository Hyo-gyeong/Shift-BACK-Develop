package com.project.shift.shop.service;

import com.project.shift.shop.dao.IOrderDAO;
import com.project.shift.shop.dto.*;
import com.project.shift.shop.entity.Delivery;
import com.project.shift.shop.entity.Order;
import com.project.shift.shop.entity.OrderItem;
import com.project.shift.product.entity.Product;
import com.project.shift.product.repository.ProductRepository;
import com.project.shift.shop.repository.DeliveryRepository;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

import java.time.LocalDateTime;

@Service
public class OrderService implements IOrderService {

    private final IOrderDAO orderDAO;
    private final ProductRepository productRepository;
    private final DeliveryRepository deliveryRepository;


    public OrderService(IOrderDAO orderDAO,
                        ProductRepository productRepository,
                        DeliveryRepository deliveryRepository) {
        this.orderDAO = orderDAO;
        this.productRepository = productRepository;
        this.deliveryRepository = deliveryRepository;

    }
    
    // SHOP-006 주문 생성
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
        resp.setItems(orderDTO.getItems());
        return resp;
    }
    
    // SHOP-007 주문 내역 조회
    @Override
    public OrderListResponseDTO getOrdersByUser(Long userId) {
        List<Order> orders = orderDAO.findBySenderId(userId);

        List<OrderListDTO> list = orders.stream().map(o -> {
            OrderListDTO dto = new OrderListDTO();
            dto.setOrderId(o.getOrderId());
            dto.setSenderId(o.getSenderId());
            dto.setReceiverId(o.getReceiverId());
            dto.setOrderDate(o.getOrderDate());
            dto.setTotalPrice(o.getTotalPrice());
            dto.setPointUsed(o.getPointUsed());
            dto.setCashUsed(o.getCashUsed());
            return dto;
        }).collect(Collectors.toList());

        return new OrderListResponseDTO(list);
    }
    
    // SHOP-008 주문 상세 조회
    @Override
    public OrderDetailResponseDTO getOrderDetail(Long orderId) {
        Order order = orderDAO.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("해당 주문이 없습니다. orderId=" + orderId));

        // 아이템 변환
        List<OrderDetailItemDTO> itemDTOs = order.getOrderItems().stream().map(oi -> {
            OrderDetailItemDTO dto = new OrderDetailItemDTO();
            dto.setProductId(oi.getProductId());
            dto.setQuantity(oi.getQuantity());
            dto.setItemPrice(oi.getItemPrice());

            // 상품 이름 조회
            Product product = productRepository.findById(oi.getProductId()).orElse(null);
            dto.setProductName(product != null ? product.getName() : null);

            return dto;
        }).collect(Collectors.toList());

        // 결제 정보
        PaymentDTO paymentDTO = new PaymentDTO(order.getCashUsed(), order.getPointUsed());

        // 배송 정보
        DeliverySimpleDTO deliveryDTO = deliveryRepository.findByOrder_OrderId(orderId)
                .map(d -> new DeliverySimpleDTO(d.getDeliveryStatus(), d.getTrackingNumber()))
                .orElse(null);

        // 최종 응답
        OrderDetailResponseDTO resp = new OrderDetailResponseDTO();
        resp.setOrderId(order.getOrderId());
        resp.setSenderId(order.getSenderId());
        resp.setReceiverId(order.getReceiverId());
        resp.setOrderDate(order.getOrderDate());
        resp.setItems(itemDTOs);
        resp.setTotalPrice(order.getTotalPrice());
        resp.setPayment(paymentDTO);
        resp.setDelivery(deliveryDTO);

        return resp;
    }
    
    // SHOP-009 결제 요청
    
    // SHOP-010 결제 결과 조회
    
    // SHOP-011 포인트 사용/적립 내역 조회
    
    // SHOP-012 주문 취소
    
    // SHOP-016 금액권 주문 생성
    
    // SHOP-017 금액권 결제 완료 (포인트 적립)
    
}
