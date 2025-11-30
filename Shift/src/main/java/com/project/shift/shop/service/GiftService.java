package com.project.shift.shop.service;

import com.project.shift.product.dao.IImageDAO;
import com.project.shift.product.dao.IProductDAO;
import com.project.shift.product.entity.Image;
import com.project.shift.product.entity.Product;
import com.project.shift.shop.dao.IOrderDAO;
import com.project.shift.shop.dto.gift.GiftListResponseDTO;
import com.project.shift.shop.entity.Order;
import com.project.shift.user.dao.IUserDAO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class GiftService implements IGiftService{

    private final IOrderDAO orderDAO;
    private final IProductDAO productDAO;
    private final IUserDAO userDAO;
    private final IImageDAO imageDAO;

    public GiftService(IOrderDAO orderDAO, IProductDAO productDAO, IUserDAO userDAO, IImageDAO imageDAO) {
        this.orderDAO = orderDAO;
        this.productDAO = productDAO;
        this.userDAO = userDAO;
        this.imageDAO = imageDAO;
    }

    @Override
    public List<GiftListResponseDTO> getSentGifts(Long myUserId) {

        // order 테이블에서 조건에 맞는 주문 조회(내가 보낸 선물)
        List<Order> orders = orderDAO.findBySenderId(myUserId);
        if (orders.isEmpty()) {
            return new ArrayList<>();
        }

        // 상품id 수집
        Set<Long> productIds = new HashSet<>();
        
        // 받는 사람id 수집
        Set<Long> receiverIds = new HashSet<>();

        for (Order order : orders) {
            // 주문 상품이 없는 경우 건너뜀
            if (order.getOrderItems() != null && !order.getOrderItems().isEmpty()) {
                productIds.add(order.getOrderItems().getFirst().getProductId());
            }
            if (order.getReceiverId() != null) {
                receiverIds.add(order.getReceiverId());
            }
        }

        // product 테이블에서 상품 정보 조회
        Map<Long, Product> productMap = new HashMap<>();
        for (Long pid : productIds) {
            Product p = productDAO.findById(pid);
            if (p != null) productMap.put(pid, p);
        }

        // users 테이블에서 받는 사람 이름 조회
        Map<Long, String> receiverMap = new HashMap<>();
        for(Long id : receiverIds) {
            userDAO.findById(id).ifPresent(u ->
                    receiverMap.put(id, u.getName())
            );
        }

        // image 테이블에서 대표 이미지 URL 조회
        Map<Long, String> imageMap = new HashMap<>();
        for(Long pid : productIds) {
            List<Image> images = imageDAO.findByProductId(pid);
            String url = images.stream()
                    .filter(img -> "Y".equals(img.getIsRepresentative()))
                    .findFirst()
                    .map(Image::getImageUrl)
                    .orElse(null); // 이미지가 없으면 null
            imageMap.put(pid, url);
        }

        // 결과값을 반환할 DTO 리스트 생성
        List<GiftListResponseDTO> giftList = new ArrayList<>();

        for (Order order : orders) {
            if (order.getOrderItems() == null || order.getOrderItems().isEmpty()) {
                continue;
            }

            Long productId = order.getOrderItems().getFirst().getProductId();

            // 상품 정보 조회
            Product product = productMap.get(productId);
            if (product == null) {
                continue; // 상품이 없으면 건너뜀
            }

            // 받는 사람 이름 조회
            String receiverName = receiverMap.getOrDefault(order.getReceiverId(), "알 수 없음");
            String thumbUrl = imageMap.get(productId);

            // 타입 결정
            String type = (product.getCategory().getCategoryId() == 3L) ? "POINT" : "PRODUCT";

            // DTO 생성
            GiftListResponseDTO dto = GiftListResponseDTO.builder()
                    .orderId(order.getOrderId())
                    .productName(product.getName())
                    .receiverName(receiverName)
                    .imageUrl(thumbUrl)
                    .status(order.getOrderStatus())
                    .orderDate(order.getOrderDate())
                    .giftType(type)
                    .build();
            giftList.add(dto);
        }

        return giftList;
    }

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

        for (Order order : orders) {
            // 주문 상품이 없는 경우 건너뜀
            if (order.getOrderItems() != null && !order.getOrderItems().isEmpty()) {
                productIds.add(order.getOrderItems().getFirst().getProductId());
            }
            if (order.getSenderId() != null) {
                senderIds.add(order.getSenderId());
            }
        }

        // product 테이블에서 상품 정보 조회
        // 상품 정보
        Map<Long, Product> productMap = new HashMap<>();
        for (Long pid : productIds) {
            Product p = productDAO.findById(pid);
            if (p != null) productMap.put(pid, p);
        }

        // users 테이블에서 보낸 사람 이름 조회
        // 보낸 사람 이름
        Map<Long, String> senderNameMap = new HashMap<>();
        for (Long sid : senderIds) {
            userDAO.findById(sid).ifPresent(u ->
                    senderNameMap.put(sid, u.getName())
            );
        }

        // image 테이블에서 대표 이미지 URL 조회
        // 이미지 URL
        Map<Long, String> imageMap = new  HashMap<>();
        for (Long pid : productIds) {
            List<Image> images = imageDAO.findByProductId(pid);
            String url = images.stream()
                    .filter(img -> "Y".equals(img.getIsRepresentative()))
                    .findFirst()
                    .map(Image::getImageUrl)
                    .orElse(null); // 이미지가 없으면 null
            imageMap.put(pid, url);
        }

        // 결과값을 반환할 DTO 리스트 생성
        List<GiftListResponseDTO> result = new ArrayList<>();

        for (Order order : orders) {
            // 방어 로직
            if (order.getOrderItems() == null || order.getOrderItems().isEmpty()) {
                continue; // 주문 항목이 없으면 건너뜀
            }

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
                    .status(order.getOrderStatus())
                    .orderDate(order.getOrderDate())
                    .giftType(type)
                    .build();

            result.add(dto);
        }
        return result;
    }
}
