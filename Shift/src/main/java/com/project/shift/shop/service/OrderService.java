package com.project.shift.shop.service;

import com.project.shift.shop.dao.IOrderDAO;
import com.project.shift.shop.dto.*;
import com.project.shift.shop.entity.Delivery;
import com.project.shift.shop.entity.Order;
import com.project.shift.shop.entity.OrderItem;
import com.project.shift.product.entity.Product;
import com.project.shift.user.entity.UserEntity;
import com.project.shift.product.repository.ProductRepository;
import com.project.shift.shop.repository.DeliveryRepository;
import com.project.shift.shop.repository.OrderRepository;
import com.project.shift.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;




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


    public OrderService(IOrderDAO orderDAO,
                        ProductRepository productRepository,
                        DeliveryRepository deliveryRepository, 
                        OrderRepository orderRepository,
                        UserRepository userRepository) {
        this.orderDAO = orderDAO;
        this.productRepository = productRepository;
        this.deliveryRepository = deliveryRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;

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
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "존재하지 않는 주문입니다."
                ));

        // 2) 금액/포인트 검증
        int amount = requestDTO.getAmount();
        if (amount <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "결제 금액은 0보다 커야 합니다."
            );
        }

        Integer rawPointUsed = requestDTO.getPointUsed();
        int pointUsed = (rawPointUsed == null ? 0 : rawPointUsed);
        if (pointUsed < 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "포인트 사용 금액은 음수가 될 수 없습니다."
            );
        }

        int cashAmount = amount - pointUsed;
        if (cashAmount < 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "포인트 사용 금액이 결제 금액을 초과했습니다."
            );
        }

        // 주문 금액과 결제 금액 일치 여부
        if (!order.getTotalPrice().equals(amount)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "결제 금액이 주문 금액과 일치하지 않습니다."
            );
        }

        // 3) sender 포인트 확인
        UserEntity sender = userRepository.findById(order.getSenderId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "존재하지 않는 사용자입니다."
                ));

        int currentPoints = (sender.getPoints() == null ? 0 : sender.getPoints());
        if (pointUsed > currentPoints) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "보유 포인트가 부족합니다."
            );
        }

        // 4) 포인트 차감 + 주문 업데이트
        int remainPoints = currentPoints - pointUsed;

        sender.setPoints(remainPoints);
        userRepository.save(sender);

        order.setPointUsed(pointUsed);
        order.setCashUsed(cashAmount);
        order.setRemainPoints(remainPoints);
        order.setOrderStatus("S"); // 결제 성공 상태
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
    @Override
    @Transactional(readOnly = true)
    public PaymentResultDTO getPaymentResult(Long orderId) {

    	Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "존재하지 않는 주문입니다."
                ));

        Integer cashUsed = order.getCashUsed() == null ? 0 : order.getCashUsed();
        Integer pointUsed = order.getPointUsed() == null ? 0 : order.getPointUsed();

        String statusCode = order.getOrderStatus(); // P / S / C
        String status;
        LocalDateTime approvedAt = null;

        switch (statusCode) {
            case "S":
                status = "SUCCESS";
                approvedAt = order.getOrderDate();   
                break;
            case "C":
                status = "CANCELED";
                break;
            case "P":
            default:
                status = "PENDING";
                break;
        }

        PaymentResultDTO dto = new PaymentResultDTO();
        dto.setOrderId(order.getOrderId());
        dto.setCashAmount(cashUsed);
        dto.setPointUsed(pointUsed);
        dto.setStatus(status);
        dto.setApprovedAt(approvedAt);

        return dto;
    }
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

        order.setOrderStatus("C");

        return new OrderCancelResponseDTO(orderId, true);
    }
    
    // SHOP-013 환불 요청
    @Override
    @Transactional
    public RefundResponseDTO requestRefund(RefundRequestDTO requestDTO) {

        // 1) 주문 조회
        Order order = orderRepository.findById(requestDTO.getOrderId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));

        // 2) 상태 체크: 결제 완료(S)인 주문만 환불 가능
        if (!"S".equals(order.getOrderStatus())) {
            throw new IllegalArgumentException("결제 완료된 주문만 환불할 수 있습니다.");
        }

        // 3) 실제 결제 금액(현금 + 포인트) 계산
        int cashUsed = (order.getCashUsed() == null ? 0 : order.getCashUsed());
        int pointUsed = (order.getPointUsed() == null ? 0 : order.getPointUsed());
        int paidTotal = cashUsed + pointUsed;

        // 4) 요청 amount 검증 (전체환불만 허용)
        Integer requestedAmount = requestDTO.getAmount();
        if (requestedAmount == null || requestedAmount <= 0) {
            throw new IllegalArgumentException("환불 금액은 0보다 커야 합니다.");
        }
        if (!requestedAmount.equals(paidTotal)) {
            throw new IllegalArgumentException("환불 금액이 결제 금액과 일치하지 않습니다.");
        }

        // 5) sender 포인트 환불 처리 (결제 때 사용한 포인트를 다시 적립)
        UserEntity sender = userRepository.findById(order.getSenderId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        int currentPoints = sender.getPoints() == null ? 0 : sender.getPoints();
        int pointRefunded = pointUsed;
        int cashRefunded = cashUsed;

        int newPoints = currentPoints + pointRefunded;
        sender.setPoints(newPoints);
        userRepository.save(sender);

        // 6) 주문 상태 변경 (C: 취소/환불 완료)
        order.setOrderStatus("C");
        order.setRemainPoints(newPoints);
        orderRepository.save(order);

        // 7) 배송 상태 변경
        deliveryRepository.findByOrder_OrderId(order.getOrderId())
                .ifPresent(delivery -> {                   
                    if (!"C".equals(delivery.getDeliveryStatus())) {
                        delivery.setDeliveryStatus("C");   
                        deliveryRepository.save(delivery);
                    }
                });
        
        // 8) 응답 DTO 구성
        RefundResponseDTO resp = new RefundResponseDTO();
        resp.setOrderId(order.getOrderId());
        resp.setCashRefunded(cashRefunded);
        resp.setPointRefunded(pointRefunded);
        resp.setStatus("REFUNDED");
        resp.setRefundedAt(LocalDateTime.now());
        resp.setResult(true);

        return resp;
    }
    
    // SHOP-016 금액권 주문 생성
    
    // SHOP-017 금액권 결제 완료 (포인트 적립)
    
}
