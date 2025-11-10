/**package com.project.shift.shop.dao;

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


    public List<CartItemDTO> findByUserId(Long userId) {

        String sql = """
                SELECT
                    c.cart_items_id AS cart_id,
                    c.product_id    AS product_id,
                    p.product_name  AS product_name,
                    c.quantity      AS quantity,
                    c.price         AS price,
                    NULL AS image_url
                FROM cart_items c
                JOIN products p ON c.product_id = p.product_id
                WHERE c.user_id = ?
                ORDER BY c.cart_items_id DESC
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
 // ===== SHOP-002: 장바구니에 상품 추가 =====
    public CartItemDTO insertCartItem(Long userId, Long productId, int quantity) {

        // 상품 가격 가져오기
        String priceSql = "SELECT price, product_name FROM products WHERE product_id = ?";
        CartItemDTO productInfo = jdbcTemplate.queryForObject(
                priceSql,
                new Object[]{productId},
                (rs, rowNum) -> {
                    CartItemDTO dto = new CartItemDTO();
                    dto.setPrice(rs.getInt("price"));          // 단가
                    dto.setProductName(rs.getString("product_name"));
                    return dto;
                }
        );
        int unitPrice = productInfo.getPrice();
        int totalPrice = unitPrice * quantity;

        String insertSql = """
                INSERT INTO cart_items
                    (cart_items_id, product_id, user_id, quantity, price)
                VALUES
                    (seq_cart.NEXTVAL, ?, ?, ?, ?)
                """;

        jdbcTemplate.update(insertSql,
                productId,
                userId,
                quantity,
                totalPrice
        );

        // 방금 넣은 cart_items_id 가져오기
        Long newCartId = jdbcTemplate.queryForObject(
                "SELECT seq_cart.CURRVAL FROM dual",
                Long.class
        );

        // 응답 DTO 구성
        CartItemDTO result = new CartItemDTO();
        result.setCartId(newCartId);
        result.setProductId(productId);
        result.setProductName(productInfo.getProductName());
        result.setQuantity(quantity);
        result.setPrice(totalPrice);
        // 이미지 경로는 null --나중에
        result.setImageUrl(null);

        return result;
    }
}
*/
