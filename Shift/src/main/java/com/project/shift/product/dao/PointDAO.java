package com.project.shift.product.dao;

import com.project.shift.product.entity.Product;
import com.project.shift.product.entity.PointTransaction;
import com.project.shift.product.repository.PointRepository;
import com.project.shift.product.repository.PointTransactionRepository;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * [DAO-003] 금액권(POINT) 관련 데이터 접근 클래스
 * ---------------------------------------------------------
 * - PROD-011 : 금액권 상품 조회
 * - SHOP-016 : 금액권 주문 생성
 * - SHOP-017 : 금액권 결제 완료 (포인트 적립)
 * - SHOP-011 : 포인트 사용/적립 내역 조회
 * ---------------------------------------------------------
 * ※ category_id = 3 인 상품을 금액권 템플릿으로 간주
 */
@Repository
public class PointDAO implements IPointDAO {

    private final PointRepository pointRepository;
    private final PointTransactionRepository pointTransactionRepository;  
    private final EntityManager em;

    public PointDAO(PointRepository pointRepository,
                    PointTransactionRepository pointTransactionRepository,
                    EntityManager em) {
        this.pointRepository = pointRepository;
        this.pointTransactionRepository = pointTransactionRepository; 
        this.em = em;
    }

    /** [PROD-011] 금액권(category_id=3) 상품 조회 */
    @Override
    public Product findPointTemplate() {
        return pointRepository.findPointProduct();
    }

    /** [SHOP-017] 포인트 적립: users.points 증가 */
    @Override
    public int addPoints(Long userId, Integer amount) {
        String sql = """
                UPDATE users 
                   SET points = points + :amount
                 WHERE user_id = :userId
                """;

        int updated = em.createNativeQuery(sql)
                .setParameter("amount", amount)
                .setParameter("userId", userId)
                .executeUpdate();

        if (updated == 0)
            throw new IllegalArgumentException("사용자 없음: " + userId);

        Integer total = (Integer) em.createNativeQuery(
                "SELECT points FROM users WHERE user_id = :userId")
                .setParameter("userId", userId)
                .getSingleResult();

        return total;
    }

    /** [SHOP-017] 포인트 거래내역 insert */
    @Override
    public void insertPointTransaction(Long userId, Integer amount, String type) {

        String sql = """
                INSERT INTO point_transactions
                (point_transaction_id, user_id, transaction_type, amount, created_at)
                VALUES (seq_point_transactions.NEXTVAL, :userId, :type, :amount, SYSTIMESTAMP)
                """;

        em.createNativeQuery(sql)
                .setParameter("userId", userId)
                .setParameter("type", type)
                .setParameter("amount", amount)
                .executeUpdate();
    }

    /** [SHOP-011] userId의 포인트 전체 이력 조회 */
    @Override
    public List<PointTransaction> findTransactions(Long userId) {
        return pointTransactionRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }
}
