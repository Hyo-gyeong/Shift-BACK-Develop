package com.project.shift.shop.dao;

import com.project.shift.shop.dto.CartItemDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CartDAO {  

    private final JdbcTemplate jdbcTemplate;

    public CartDAO(JdbcTemplate jdbcTemplate) {   
        this.jdbcTemplate = jdbcTemplate;
    }

    // SHOP-001 : userId로 장바구니 목록 조회
    public List<CartItemDTO> findByUserId(Long userId) {

        String sql = """
                SELECT
                    c.cart_id        AS cart_id,
                    c.product_id     AS product_id,
                    p.product_name   AS product_name,
                    c.quantity       AS quantity,
                    c.price          AS price,
                    NULL AS image_url
                FROM cart c
                JOIN products p ON c.product_id = p.product_id
                WHERE c.user_id = ?
                ORDER BY c.cart_id DESC
                """;

        return jdbcTemplate.query(sql, new Object[]{userId}, (rs, rowNum) -> {
            CartItemDTO dto = new CartItemDTO();
            dto.setCartId(rs.getLong("cart_id"));
            dto.setProductId(rs.getLong("product_id"));
            dto.setProductName(rs.getString("product_name"));
            dto.setQuantity(rs.getInt("quantity"));
            dto.setPrice(rs.getInt("price"));
            dto.setImageUrl(rs.getString("image_url"));
            return dto;
        });
    }
}
