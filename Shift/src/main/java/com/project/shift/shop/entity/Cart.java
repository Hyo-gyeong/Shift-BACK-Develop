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

    // user_id FK
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    // product_id FK
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "quantity")
    private Integer quantity;

    // 담았을 때의 가격
    @Column(name = "price")
    private Integer price;
}
