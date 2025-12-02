package com.project.shift.product.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.shift.product.dto.UserReviewDetailProjection;
import com.project.shift.product.entity.Review;

/**
 * [REP-004] 리뷰 Repository
 * ---------------------------------------------------------
 * - PROD-008 : 특정 상품 리뷰 조회
 */
public interface ReviewRepository extends JpaRepository<Review, Long> {

    /** [PROD-008] 특정 상품 ID 기준 리뷰 조회 (최신 작성일 순) */
    List<Review> findByProduct_IdOrderByCreatedDateDesc(Long productId);
    
    /** [PROD-009] 특정 사용자가 작성한 모든 디테일 리뷰 목록 조회 (최신 작성일 순) */
    @Query(value = """
    		select r.review_id as reviewId,
			       r.rating, 
			       r.content, 
			       r.created_date as createdDate, 
			       o.quantity, 
			       o.item_price as itemPrice, 
			       p.product_name as productName, 
			       p.price, 
			       p.seller,
			       i.image_url as imageUrl
			from reviews r
			join order_items o on r.order_item_id = o.order_item_id
			join products p on p.product_id = o.product_id
			join images i on i.product_id = p.product_id
			where r.user_id = :userId
			    and i.is_representative = 'N'
			order by r.created_date desc
    		""", nativeQuery = true)
    List<UserReviewDetailProjection> findUserReviewDetails(@Param("userId") Long userId);
    
    /** [PROD-013] 리뷰 작성 여부 + 작성 가능 여부 확인 */
    @Query(value = """
    	    SELECT COUNT(*)
    	    FROM orders o
    	    JOIN deliveries d ON o.order_id = d.order_id
    	    JOIN order_items oi ON o.order_id = oi.order_id
    	    WHERE o.receiver_id = :userId
    	      AND oi.order_item_id = :orderItemId
    	      AND o.order_status = 'D'
    	      AND d.delivery_status = 'D'
    	""", nativeQuery = true)
    	int countDeliveredOrderItem(@Param("userId") Long userId,
    	                            @Param("orderItemId") Long orderItemId);
    boolean existsByOrderItem_OrderItemId(Long orderItemId);
}