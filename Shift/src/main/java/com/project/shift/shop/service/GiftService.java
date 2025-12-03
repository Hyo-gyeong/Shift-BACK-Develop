package com.project.shift.shop.service;

import com.project.shift.product.dao.IImageDAO;
import com.project.shift.product.dao.IProductDAO;
import com.project.shift.product.entity.Image;
import com.project.shift.product.entity.Product;
import com.project.shift.shop.dao.IDeliveryDAO;
import com.project.shift.shop.dao.IOrderDAO;
import com.project.shift.shop.dto.gift.GiftDetailResponseDTO;
import com.project.shift.shop.dto.gift.GiftListResponseDTO;
import com.project.shift.shop.entity.Delivery;
import com.project.shift.shop.entity.Order;
import com.project.shift.shop.entity.OrderItem;
import com.project.shift.user.dao.IUserDAO;
import com.project.shift.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class GiftService implements IGiftService {

    private final IOrderDAO orderDAO;
    private final IProductDAO productDAO;
    private final IUserDAO userDAO;
    private final IImageDAO imageDAO;
    private final IDeliveryDAO deliveryDAO;

    // 받은 선물 조회
    @Override
    @Transactional(readOnly = true)
    public List<GiftListResponseDTO> getReceivedGifts(Long userId) {

        // order 테이블 에서 조건에 맞는 주문 조회
        // 조건에 맞는 주문 목록 조회
        List<Order> orders = orderDAO.findReceivedGifts(userId);
        if (orders.isEmpty()) {
            return new ArrayList<>();
        }

        // 상품id 수집
        Set<Long> productIds = new HashSet<>();

        // 보낸 사람id 수집
        Set<Long> senderIds = new HashSet<>();

        // 유효한 주문 목록
        List<Order> validOrders = new ArrayList<>();

        for (Order order : orders) {
            // 주문 상품이 없는 경우 건너뜀
            if (order.getOrderItems() == null || order.getOrderItems().isEmpty()) {
                continue;
            }

            // 본인에게 보낸 선물은 제외
            if (order.getSenderId().equals(userId)) {
                continue;
            }

            validOrders.add(order);
            productIds.add(order.getOrderItems().getFirst().getProductId());
            senderIds.add(order.getSenderId());
        }

        // 유효한 주문이 없으면 빈 리스트 반환
        if (validOrders.isEmpty()) {
            return new ArrayList<>();
        }

        // product 테이블에서 상품 정보 조회
        // 상품 정보
        Map<Long, Product> productMap = getProductMap(productIds);

        // users 테이블에서 보낸 사람 이름 조회
        // 보낸 사람 이름
        Map<Long, String> senderNameMap = getUserNameMap(senderIds);

        // image 테이블에서 대표 이미지 URL 조회
        // 이미지 URL
        Map<Long, String> imageMap = getImageUrlMap(productIds);

        // 결과값을 반환할 DTO 리스트 생성
        List<GiftListResponseDTO> result = new ArrayList<>();

        for (Order order : validOrders) {

            Long productId = order.getOrderItems().getFirst().getProductId();

            // 상품 정보 조회
            Product product = productMap.get(productId);
            if (product == null) {
                continue; // 상품이 없으면 건너뜀
            }

            // 보낸 사람 이름 조회
            String senderName = senderNameMap.getOrDefault(order.getSenderId(), "알 수 없음");
            String thumbUrl = imageMap.get(productId);

            // 타입 결정
            String type = (product.getCategory().getCategoryId() == 3L) ? "POINT" : "PRODUCT";

            // DTO 생성
            GiftListResponseDTO dto = GiftListResponseDTO.builder()
                    .orderId(order.getOrderId())
                    .productName(product.getName())
                    .senderName(senderName)
                    .imageUrl(thumbUrl)
                    .orderDate(order.getOrderDate())
                    .giftType(type)
                    .build();

            result.add(dto);
        }
        return result;
    }

    @Override
    public GiftDetailResponseDTO getDetailGift(Long userId, Long orderId) {

        // order 정보 조회
        Order order = orderDAO.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));

        Long senderId = order.getSenderId();
        Long receiverId = order.getReceiverId();

        // 접근 권한 확인
        if (!userId.equals(receiverId)) {
            throw new IllegalArgumentException("해당 선물에 대한 접근 권한이 없습니다.");
        }

        if (order.getOrderItems() == null || order.getOrderItems().isEmpty()) {
            throw new IllegalArgumentException("해당 주문에 상품이 없습니다.");
        }
        
        OrderItem orderItem = order.getOrderItems().getFirst();

        // product 정보 조회
        Long productId = orderItem.getProductId();
        Integer quantity = orderItem.getQuantity();

        Product product = productDAO.findById(productId);
        if (product == null) {
            throw new IllegalArgumentException("해당 상품이 존재하지 않습니다.");
        }

        // image 정보 조회
        List<Image> images = imageDAO.findByProductId(productId);
        String imageUrl = images.stream()
                .filter(img -> "Y".equals(img.getIsRepresentative()))
                .findFirst()
                .map(Image::getImageUrl)
                .orElse(null); // 이미지가 없으면 null

        // user 정보 조회
        String senderName = userDAO.findById(senderId)
                .map(UserEntity::getName).orElse("알 수 없음");

        // 배송지 정보 조회
        String deliveryAddress = null;
        String deliveryStatus = "P"; // 기본값 설정

        try {
            Delivery delivery = deliveryDAO.findByOrderId(orderId);
            if (delivery != null) {
                // 배송 상태 및 주소 설정
                deliveryStatus = delivery.getDeliveryStatus();
                deliveryAddress = delivery.getDeliveryAddress();
            }
        } catch (Exception e) {
            // 배송 정보가 없을 경우 무시
        }

        return GiftDetailResponseDTO.builder()
                .orderId(order.getOrderId())
                .productId(orderItem.getProductId())
                .orderItemId(orderItem.getOrderItemId())
                .productName(product.getName())
                .imageUrl(imageUrl)
                .senderName(senderName)
                .orderStatus(order.getOrderStatus())
                .deliveryStatus(deliveryStatus)
                .quantity(quantity)
                .deliveryAddress(deliveryAddress)
                .build();
    }

    // 상품 정보 조회
    private Map<Long, Product> getProductMap(Set<Long> productIds) {
        Map<Long, Product> productMap = new HashMap<>();
        for (Long pid : productIds) {
            Product p = productDAO.findById(pid);
            if (p != null) productMap.put(pid, p);
        }
        return productMap;
    }

    // 사용자 이름 조회
    private Map<Long, String> getUserNameMap(Set<Long> userIds) {
        Map<Long, String> userNameMap = new HashMap<>();
        for (Long id : userIds) {
            userDAO.findById(id).ifPresent(u ->
                    userNameMap.put(id, u.getName())
            );
        }
        return userNameMap;
    }

    // 이미지 URL 조회
    private Map<Long, String> getImageUrlMap(Set<Long> productIds) {
        Map<Long, String> imageMap = new HashMap<>();
        for (Long pid : productIds) {
            List<Image> images = imageDAO.findByProductId(pid);
            String url = images.stream()
                    .filter(img -> "Y".equals(img.getIsRepresentative()))
                    .findFirst()
                    .map(Image::getImageUrl)
                    .orElse(null);
            imageMap.put(pid, url);
        }
        return imageMap;
    }
}
