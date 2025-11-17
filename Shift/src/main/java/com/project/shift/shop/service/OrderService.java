package com.project.shift.shop.service;

import com.project.shift.shop.dao.*;
import com.project.shift.shop.dto.*;
import com.project.shift.shop.entity.*;
import com.project.shift.shop.repository.*;
import com.project.shift.chat.entity.*;
import com.project.shift.chat.repository.*;
import com.project.shift.product.entity.*;
import com.project.shift.product.dao.*;
import com.project.shift.product.repository.*;
import com.project.shift.user.entity.*;
import com.project.shift.user.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import java.time.LocalDateTime;

@Service
public class OrderService implements IOrderService {

    private final IOrderDAO orderDAO;
    private final ProductRepository productRepository;
    private final DeliveryRepository deliveryRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ChatroomRepository chatroomRepository;
    private final IPointDAO pointDAO;

    public OrderService(IOrderDAO orderDAO,
            ProductRepository productRepository,
            DeliveryRepository deliveryRepository,
            OrderRepository orderRepository,
            UserRepository userRepository,
            ChatroomRepository chatroomRepository,
            IPointDAO pointDAO) {

	this.orderDAO = orderDAO;
	this.productRepository = productRepository;
	this.deliveryRepository = deliveryRepository;
	this.orderRepository = orderRepository;
	this.userRepository = userRepository;
	this.chatroomRepository = chatroomRepository;
	this.pointDAO = pointDAO;
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
    @Override
    @Transactional
    public PaymentResponseDTO requestPayment(PaymentRequestDTO requestDTO) {

        // 1) 주문 조회
        Order order = orderRepository.findById(requestDTO.getOrderId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));

        // 2) 금액/포인트 검증
        int amount = requestDTO.getAmount();
        if (amount <= 0) {
            throw new IllegalArgumentException("결제 금액은 0보다 커야 합니다.");
        }

        Integer rawPointUsed = requestDTO.getPointUsed();
        int pointUsed = (rawPointUsed == null ? 0 : rawPointUsed);
        if (pointUsed < 0) {
            throw new IllegalArgumentException("포인트 사용 금액은 음수가 될 수 없습니다.");
        }

        int cashAmount = amount - pointUsed;
        if (cashAmount < 0) {
            throw new IllegalArgumentException("포인트 사용 금액이 결제 금액을 초과했습니다.");
        }

        // 주문 금액과 결제 금액 일치 여부
        if (!order.getTotalPrice().equals(amount)) {
            throw new IllegalArgumentException("결제 금액이 주문 금액과 일치하지 않습니다.");
        }

        // 3) sender 포인트 확인
        UserEntity sender = userRepository.findById(order.getSenderId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        int currentPoints = (sender.getPoints() == null ? 0 : sender.getPoints());
        if (pointUsed > currentPoints) {
            throw new IllegalArgumentException("보유 포인트가 부족합니다.");
        }

        // 4) 포인트 차감 + 주문 업데이트
        int remainPoints = currentPoints - pointUsed;

        sender.setPoints(remainPoints);
        userRepository.save(sender);

        order.setPointUsed(pointUsed);
        order.setCashUsed(cashAmount);
        order.setRemainPoints(remainPoints);
        // 결제 완료 상태를 따로 안 두면 orderStatus는 그대로 'P' 유지
        orderRepository.save(order);

        // 5) 응답 DTO 구성
        PaymentResponseDTO response = new PaymentResponseDTO();
        response.setOrderId(order.getOrderId());
        response.setCashAmount(cashAmount);
        response.setPointUsed(pointUsed);
        response.setStatus("SUCCESS");
        response.setApprovedAt(LocalDateTime.now());

        return response;
    }

    // SHOP-010 결제 결과 조회
    
    // SHOP-011 포인트 사용/적립 내역 조회
    
    // SHOP-012 주문 취소
    @Override
    @Transactional
    public OrderCancelResponseDTO cancelOrder(Long orderId) {
        Order order = orderDAO.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다. order_id=" + orderId));

        // 배송 전 상태만 취소 
        if (!"P".equals(order.getOrderStatus())) {
            
            return new OrderCancelResponseDTO(orderId, false);
        }

        order.setOrderStatus("C");  // 취소

        return new OrderCancelResponseDTO(orderId, true);
    }
    // SHOP-016 금액권 주문 생성
    @Override
    @Transactional
    public PointOrderResponseDTO createPointOrder(PointOrderRequestDTO requestDTO) {

        Long senderId = requestDTO.getSenderId();
        Long chatroomId = requestDTO.getChatroomId();
        Long categoryId = requestDTO.getCategoryId();   // categoryId=3
        Integer amount = requestDTO.getAmount();

        // 1) 채팅방 조회 및 receiverId 계산
        ChatroomEntity chatroom = chatroomRepository.findById(chatroomId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채팅방입니다."));

        Long receiverId;
        if (chatroom.getFromUserId().equals(senderId)) {
            receiverId = chatroom.getToUserId();
        } else if (chatroom.getToUserId().equals(senderId)) {
            receiverId = chatroom.getFromUserId();
        } else {
            throw new IllegalArgumentException("해당 채팅방에 senderId가 존재하지 않습니다.");
        }

        // 2) 금액권 카테고리 검증
        if (!categoryId.equals(3L)) {
            throw new IllegalArgumentException("금액권 카테고리가 아닙니다. categoryId=3 필요");
        }

        // 3) 금액권 상품 조회(category_id=3)
        Product pointProduct = pointDAO.findPointTemplate();
        if (pointProduct == null) {
            throw new IllegalStateException("금액권(product) 상품이 존재하지 않습니다.");
        }

        Long productId = pointProduct.getId();

        // 4) 주문 생성
        Order order = new Order();
        order.setSenderId(senderId);
        order.setReceiverId(receiverId);
        order.setOrderDate(LocalDateTime.now());
        order.setOrderStatus("P");
        order.setTotalPrice(amount);
        order.setCashUsed(amount);
        order.setPointUsed(0);

        // 5) 주문 상세 추가
        OrderItem item = new OrderItem();
        item.setProductId(productId);
        item.setQuantity(1);
        item.setItemPrice(amount);

        order.addItem(item);

        Order saved = orderDAO.save(order);

        // 6) 응답 생성
        PointOrderResponseDTO res = new PointOrderResponseDTO();
        res.setOrderId(saved.getOrderId());
        res.setSenderId(senderId);
        res.setReceiverId(receiverId);
        res.setChatroomId(chatroomId);
        res.setAmount(amount);
        res.setStatus("P");
        res.setResult(true);

        return res;
    }

    // SHOP-017 금액권 결제 완료 (포인트 적립)
    @Override
    @Transactional
    public PointOrderCompleteDTO completePointOrder(Long orderId, Long chatroomId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));

        // 🔒 이미 완료된 주문이면 중복 적립 차단
        if (!"P".equals(order.getOrderStatus())) {
            throw new IllegalStateException("이미 결제가 완료된 주문입니다.");
        }

        Long receiverId = order.getReceiverId();
        Integer price = order.getTotalPrice();

        UserEntity receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        int before = receiver.getPoints() == null ? 0 : receiver.getPoints();
        int after = before + price;

        receiver.setPoints(after);
        userRepository.save(receiver);

        // 주문 상태 변경
        order.setOrderStatus("S");
        order.setRemainPoints(after);
        orderRepository.save(order);

        PointOrderCompleteDTO dto = new PointOrderCompleteDTO();
        dto.setOrderId(orderId);
        dto.setChatroomId(chatroomId);
        dto.setReceiverId(receiverId);
        dto.setAddedPoints(price);
        dto.setUpdatedTotalPoints(after);
        dto.setResult(true);

        return dto;
    }
}
