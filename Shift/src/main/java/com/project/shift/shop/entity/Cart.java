package com.project.shift.shop.entity;

import com.project.shift.product.entity.Product;
import com.project.shift.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cart_items") 
@SequenceGenerator(
        name = "SEQ_CART_ITEMS_GENERATOR",
        sequenceName = "seq_cart_items",  
        allocationSize = 1
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CART_ITEMS_GENERATOR")
    @Column(name = "cart_items_id")
    private Long id;

    // 사용자 정보 (FK: user_id)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    // 상품 정보 (FK: product_id)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    // 수량
    @Column(nullable = false)
    private Integer quantity;

    // 담을 당시 가격
    @Column(nullable = false)
    private Integer price;
}
