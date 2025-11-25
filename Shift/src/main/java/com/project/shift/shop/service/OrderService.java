package com.project.shift.shop.service;

import static com.project.shift.global.security.CurrentUser.getUserIdOrNull;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.HashMap;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.project.shift.chat.dto.MessageDTO;
import com.project.shift.chat.entity.ChatroomEntity;
import com.project.shift.chat.repository.ChatroomRepository;
import com.project.shift.chat.service.MessageService;
import com.project.shift.product.dao.IPointDAO;
import com.project.shift.product.entity.PointTransaction;
import com.project.shift.product.entity.Product;
import com.project.shift.product.repository.ProductRepository;
import com.project.shift.shop.dao.IOrderDAO;
import com.project.shift.shop.dto.DeliverySimpleDTO;
import com.project.shift.shop.dto.OrderCancelResponseDTO;
import com.project.shift.shop.dto.OrderDTO;
import com.project.shift.shop.dto.OrderDetailItemDTO;
import com.project.shift.shop.dto.OrderDetailResponseDTO;
import com.project.shift.shop.dto.OrderItemDTO;
import com.project.shift.shop.dto.OrderListDTO;
import com.project.shift.shop.dto.OrderListResponseDTO;
import com.project.shift.shop.dto.PaymentDTO;
import com.project.shift.shop.dto.PaymentRequestDTO;
import com.project.shift.shop.dto.PaymentResponseDTO;
import com.project.shift.shop.dto.PaymentResultDTO;
import com.project.shift.shop.dto.PointHistoryDTO;
import com.project.shift.shop.dto.PointHistoryResponseDTO;
import com.project.shift.shop.dto.PointOrderCompleteDTO;
import com.project.shift.shop.dto.PointOrderRequestDTO;
import com.project.shift.shop.dto.PointOrderResponseDTO;
import com.project.shift.shop.dto.RefundRequestDTO;
import com.project.shift.shop.dto.RefundResponseDTO;
import com.project.shift.shop.entity.Order;
import com.project.shift.shop.entity.OrderItem;
import com.project.shift.shop.repository.DeliveryRepository;
import com.project.shift.shop.repository.OrderRepository;
import com.project.shift.user.entity.UserEntity;
import com.project.shift.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor // 생성자 주입을 임의의 코드없이 자동으로 설정해주는 어노테이션
public class OrderService implements IOrderService {

    private final IOrderDAO orderDAO;
    private final IPointDAO pointDAO;
    private final ProductRepository productRepository;
    private final DeliveryRepository deliveryRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final MessageService messageService;

    private String toDisplayOrderStatus(String code) {
        if (code == null) return "PENDING";
        return switch (code) {
            case "S" -> "PAID";
            case "C" -> "CANCELED";
            case "P" -> "PENDING";
            default -> "PENDING";
        };
    }
    
    private Map.Entry<String, LocalDateTime> toPaymentStatusAndApprovedAt(String orderStatus, LocalDateTime orderDate) {
        switch (orderStatus) {
            case "S": return Map.entry("SUCCESS", orderDate);
            case "C": return Map.entry("CANCELED", null);
            case "P":
            default:  return Map.entry("PENDING", null);
        }
    }
    
    // SHOP-006 주문 생성
    @Override
    @Transactional
    public OrderDTO createOrder(OrderDTO orderDTO) {
    	   Long uid = getUserIdOrNull();
    	    if (uid != null) {
    	        orderDTO.setSenderId(uid); 
    	    }

    	    if (orderDTO.getSenderId() == null) {
    	        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인 후 주문할 수 있습니다.");
    	    }
    	    if (orderDTO.getReceiverId() == null) {
    	    	orderDTO.setReceiverId(orderDTO.getSenderId());
    	    }
    	    if (orderDTO.getItems() == null || orderDTO.getItems().isEmpty()) {
    	        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "주문 항목이 비어 있습니다.");
    	    }

    	    Order order = new Order();
    	    order.setSenderId(orderDTO.getSenderId());
    	    order.setReceiverId(orderDTO.getReceiverId());
    	    order.setOrderDate(LocalDateTime.now());
    	    order.setOrderStatus("P"); // 결제 전(P)

    	    int totalPrice = 0;

    	    for (OrderItemDTO itemDTO : orderDTO.getItems()) {
    	        Product product = productRepository.findById(itemDTO.getProductId())
    	                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다. product_id=" + itemDTO.getProductId()));

    	        int unitPrice = product.getPrice();        // 상품 현재가
    	        int linePrice = unitPrice * itemDTO.getQuantity();
    	        totalPrice += linePrice;

    	        OrderItem orderItem = new OrderItem();
    	        orderItem.setProductId(itemDTO.getProductId());
    	        orderItem.setQuantity(itemDTO.getQuantity());
    	        orderItem.setItemPrice(unitPrice);         // 단가 저장 
    	        order.addItem(orderItem);
    	    }

    	    order.setTotalPrice(totalPrice);

    	    Order saved = orderDAO.save(order);

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

        Map<Long, String> nameCache = new HashMap<>();
        for (Order o : orders) {
            nameCache.computeIfAbsent(o.getSenderId(),
                    id -> userRepository.findById(id).map(UserEntity::getName).orElse(null));
            nameCache.computeIfAbsent(o.getReceiverId(),
                    id -> userRepository.findById(id).map(UserEntity::getName).orElse(null));
        }

        List<OrderListDTO> list = orders.stream().map(o -> {
            OrderListDTO dto = new OrderListDTO();
            dto.setOrderId(o.getOrderId());
            dto.setSenderId(o.getSenderId());
            dto.setReceiverId(o.getReceiverId());
            dto.setSenderName(nameCache.get(o.getSenderId()));     
            dto.setReceiverName(nameCache.get(o.getReceiverId())); 
            dto.setOrderStatus(toDisplayOrderStatus(o.getOrderStatus())); 
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
    @Transactional(readOnly = true)
    public OrderDetailResponseDTO getOrderDetail(Long orderId) {
    	Long uid = getUserIdOrNull();
        Order order = orderDAO.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("해당 주문이 없습니다. orderId=" + orderId));

        if (uid != null && !order.getSenderId().equals(uid)) {
            throw new AccessDeniedException("본인 주문만 조회할 수 있습니다.");
        }

        List<OrderDetailItemDTO> itemDTOs = order.getOrderItems().stream().map(oi -> {
            OrderDetailItemDTO dto = new OrderDetailItemDTO();
            dto.setProductId(oi.getProductId());
            dto.setQuantity(oi.getQuantity());
            dto.setItemPrice(oi.getItemPrice());

            Product product = productRepository.findById(oi.getProductId()).orElse(null);
            dto.setProductName(product != null ? product.getName() : null);
            return dto;
        }).collect(Collectors.toList());
        
        // 결제 정보 (status/approvedAt 포함)
        Integer cash = order.getCashUsed() == null ? 0 : order.getCashUsed();
        Integer point = order.getPointUsed() == null ? 0 : order.getPointUsed();
        var pair = toPaymentStatusAndApprovedAt(order.getOrderStatus(), order.getOrderDate());
        
        PaymentDTO paymentDTO = new PaymentDTO(cash, point);
        paymentDTO.setStatus(pair.getKey());         // SUCCESS/PENDING/CANCELED
        paymentDTO.setApprovedAt(pair.getValue());   // S일 때만 orderDate
        
        DeliverySimpleDTO deliveryDTO = deliveryRepository.findByOrder_OrderId(orderId)
                .map(d -> new DeliverySimpleDTO(d.getDeliveryStatus(), d.getTrackingNumber()))
                .orElse(null);

        // 이름
        String senderName = userRepository.findById(order.getSenderId()).map(UserEntity::getName).orElse(null);
        String receiverName = userRepository.findById(order.getReceiverId()).map(UserEntity::getName).orElse(null);

        
        OrderDetailResponseDTO resp = new OrderDetailResponseDTO();
        resp.setOrderId(order.getOrderId());
        resp.setSenderId(order.getSenderId());
        resp.setReceiverId(order.getReceiverId());
        resp.setSenderName(senderName);    
        resp.setReceiverName(receiverName);
        resp.setOrderDate(order.getOrderDate());
        resp.setItems(itemDTOs);
        resp.setTotalPrice(order.getTotalPrice());
        resp.setPayment(paymentDTO);
        resp.setDelivery(deliveryDTO);
        return resp;
    }
    
    //############### 선물 구매 및 메시지 전송 ###############
    @Override
	public PaymentResponseDTO requestGiftPayment(PaymentRequestDTO requestDTO, long chatroomId, long userId) {
    	PaymentResponseDTO dto = requestPayment(requestDTO);
    	MessageDTO messageDTO = MessageDTO.builder()
				    	        .isGift("Y")
				    	        .type(MessageDTO.MessageType.CHAT)
				    	        .chatroomId(chatroomId)
				    	        .sendDate(new Date())
				    	        .unreadCount(1)
				    	        .content("선물이 도착했습니다!")
				    	        .userId(userId)
				    	        .build();
    	messageService.sendAndSaveMessage(messageDTO, null);
		return dto;
	}
    
    // SHOP-009 결제 요청
    @Override
    @Transactional
    public PaymentResponseDTO requestPayment(PaymentRequestDTO requestDTO) {
    	Long uid = getUserIdOrNull();

        Order order = orderDAO.findById(requestDTO.getOrderId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 주문입니다."));

        if (uid != null && !order.getSenderId().equals(uid)) {
            throw new AccessDeniedException("본인 주문만 결제할 수 있습니다.");
        }

        int amount = requestDTO.getAmount();
        if (amount <= 0) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "결제 금액은 0보다 커야 합니다.");

        int pointUsed = (requestDTO.getPointUsed() == null ? 0 : requestDTO.getPointUsed());
        if (pointUsed < 0) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "포인트 사용 금액은 음수가 될 수 없습니다.");

        int cashAmount = amount - pointUsed;
        if (cashAmount < 0) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "포인트 사용 금액이 결제 금액을 초과했습니다.");

        if (!order.getTotalPrice().equals(amount)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "결제 금액이 주문 금액과 일치하지 않습니다.");
        }

        UserEntity sender = userRepository.findById(order.getSenderId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."));

        int currentPoints = (sender.getPoints() == null ? 0 : sender.getPoints());
        if (pointUsed > currentPoints) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "보유 포인트가 부족합니다.");
        }

        int remainPoints = currentPoints - pointUsed;
        sender.setPoints(remainPoints);
        userRepository.save(sender);

        order.setPointUsed(pointUsed);
        order.setCashUsed(cashAmount);
        order.setRemainPoints(remainPoints);
        order.setOrderStatus("S"); // 결제 완료
        orderDAO.save(order);

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
    	Long uid = getUserIdOrNull();

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 주문입니다."));

        if (uid != null && !order.getSenderId().equals(uid)) {
            throw new AccessDeniedException("본인 주문만 조회할 수 있습니다.");
        }

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
    @Override
    @Transactional(readOnly = true)
    public PointHistoryResponseDTO getPointHistory(Long userId) {

        // 사용자 조회
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        int totalPoints = (user.getPoints() == null ? 0 : user.getPoints());

        // DAO에서 포인트 거래내역 조회
        List<PointTransaction> list = pointDAO.findTransactions(userId);

        // DTO 매핑
        List<PointHistoryDTO> history = list.stream()
                .map(t -> PointHistoryDTO.builder()
                        .transactionId(t.getId())
                        .type(t.getType())
                        .amount(t.getAmount())
                        .createdAt(t.getCreatedAt())
                        .orderId(t.getOrderId())
                        .build()
                ).toList();

        return PointHistoryResponseDTO.builder()
                .userId(userId)
                .totalPoints(totalPoints)
                .history(history)
                .result(true)
                .build();
    }

    
    // SHOP-012 주문 취소
    @Override
    @Transactional
    public OrderCancelResponseDTO cancelOrder(Long orderId) {
    	Long uid = getUserIdOrNull();

        Order order = orderDAO.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다. order_id=" + orderId));

        if (uid != null && !order.getSenderId().equals(uid)) {
            throw new AccessDeniedException("본인 주문만 취소할 수 있습니다.");
        }

        // 결제 전(P)만 취소 가능
        if (!"P".equals(order.getOrderStatus())) {
            return new OrderCancelResponseDTO(orderId, false);
        }

        // 배송 상태가 이미 진행 중이면 취소 불가
        var deliveryOpt = deliveryRepository.findByOrder_OrderId(orderId);
        if (deliveryOpt.isPresent()) {
            String ds = deliveryOpt.get().getDeliveryStatus(); // P,S,D,C
            if (!"P".equals(ds)) {
                return new OrderCancelResponseDTO(orderId, false);
            }
        }

        // 주문 취소
        order.setOrderStatus("C");
        orderDAO.save(order);

        // 배송 레코드가 있다면 함께 취소 표기
        deliveryOpt.ifPresent(d -> {
            if (!"C".equals(d.getDeliveryStatus())) {
                d.setDeliveryStatus("C");
                deliveryRepository.save(d);
            }
        });

        return new OrderCancelResponseDTO(orderId, true);
    }
    
    // SHOP-013 환불 요청
    @Override
    @Transactional
    public RefundResponseDTO requestRefund(RefundRequestDTO requestDTO) {

        // 1) 주문 조회
        Order order = orderDAO.findById(requestDTO.getOrderId())
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
        orderDAO.save(order);

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
    @Override
    @Transactional
    public PointOrderResponseDTO createPointOrder(PointOrderRequestDTO dto) {

        Long senderId = dto.getSenderId();
        if (senderId == null)
            senderId = getUserIdOrNull();

        if (senderId == null)
            throw new IllegalArgumentException("로그인 후 이용 가능합니다.");

        Long chatroomId = dto.getChatroomId();
        if (chatroomId == null)
            throw new IllegalArgumentException("chatroomId 필수");

        // receiver 조회
        Long receiverId = orderDAO.findReceiverInChatroom(chatroomId, senderId);
        if (receiverId == null)
            throw new IllegalArgumentException("대화 상대가 없습니다.");

        // 금액권 상품 검증
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("금액권 상품 없음"));

        if (product.getCategoryId() != 3)
            throw new IllegalArgumentException("금액권 상품 아님");

        // Order 생성
        Order order = new Order();
        order.setSenderId(senderId);
        order.setReceiverId(receiverId);
        order.setOrderDate(LocalDateTime.now());
        order.setTotalPrice(dto.getAmount());
        order.setCashUsed(dto.getAmount());
        order.setPointUsed(0);
        order.setRemainPoints(0);
        order.setOrderStatus("P");

        orderDAO.save(order);

        return PointOrderResponseDTO.builder()
                .orderId(order.getOrderId())
                .senderId(senderId)
                .chatroomId(chatroomId)
                .amount(dto.getAmount())
                .status("P")
                .result(true)
                .build();
    }
    
    // SHOP-017 금액권 결제 완료 (포인트 적립)
    @Override
    @Transactional
    public PointOrderCompleteDTO completePointPayment(Long orderId) {

        Order order = orderDAO.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다."));

        if (!"P".equals(order.getOrderStatus())) {
            throw new IllegalStateException("이미 결제 완료된 주문입니다.");
        }

        // 1) 상태 변경
        order.setOrderStatus("S");
        orderDAO.save(order);

        Long receiverId = order.getReceiverId();
        Integer amount = order.getTotalPrice();

        // 2) receiver 포인트 적립
        int updatedTotal = pointDAO.addPoints(receiverId, amount); 
        // addPoint = UPDATE users set points = points + amount

        // 3) 포인트 거래내역 insert
        pointDAO.insertPointTransaction(receiverId, amount, "A", orderId);

        return PointOrderCompleteDTO.builder()
                .chatroomId(null) // 필요하면 order.getChatroomId() 저장해놔야 함
                .receiverId(receiverId)
                .addedPoints(amount)
                .updatedTotalPoints(updatedTotal)
                .result(true)
                .build();
    }

}
