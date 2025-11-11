package com.project.shift.product.service;

import com.project.shift.product.dao.IPointDAO;
import com.project.shift.product.dto.PointCalcDTO;
import com.project.shift.product.dto.PointDTO;
import com.project.shift.product.entity.Product;
import com.project.shift.product.repository.PointRepository;
import org.springframework.stereotype.Service;

/**
 * [SERVICE-002] 금액권 관련 비즈니스 로직 처리 클래스
 * ---------------------------------------------------------
 * - PROD-010 : 금액권 페이지 정보 조회
 * - PROD-011 : 금액 입력 및 합계 계산
 * ---------------------------------------------------------
 * ※ category_id = 3 인 상품을 금액권 템플릿으로 사용
 */
@Service
public class PointService implements IPointService {

    private static final int DEFAULT_MIN_AMOUNT = 1000;
    private static final int DEFAULT_MAX_AMOUNT = 5000000;

    private final IPointDAO pointDAO;
    private final PointRepository pointRepository;

    public PointService(PointRepository pointRepository, IPointDAO pointDAO) {
        this.pointRepository = pointRepository;
        this.pointDAO = pointDAO;
    }

    /** [PROD-010] 금액권 페이지 기본 정보 조회 */
    @Override
    public PointDTO getPointPageInfo() {
        String imageUrl = pointRepository.findPointImage();
        if (imageUrl == null) imageUrl = "default_point_banner.jpg";

        return new PointDTO(
                "포인트 금액권",
                imageUrl,
                "선물처럼 포인트를 보내보세요.",
                DEFAULT_MIN_AMOUNT,
                DEFAULT_MAX_AMOUNT
        );
    }

    /** [PROD-011] 금액 입력 및 합계 계산 */
    @Override
    public PointCalcDTO calculatePoint(PointCalcDTO request) {
        if (request == null)
            throw new IllegalArgumentException("[PROD-011] 요청 데이터가 비어 있습니다.");

        if (request.getInputAmount() < 0 || request.getAddAmount() < 0)
            throw new IllegalArgumentException("[PROD-011] 금액은 0 이상이어야 합니다.");

        int totalAmount = request.getInputAmount() + request.getAddAmount();
        boolean withinRange = totalAmount >= DEFAULT_MIN_AMOUNT && totalAmount <= DEFAULT_MAX_AMOUNT;

        return PointCalcDTO.builder()
                .inputAmount(request.getInputAmount())
                .addAmount(request.getAddAmount())
                .totalAmount(totalAmount)
                .minPrice(DEFAULT_MIN_AMOUNT)
                .maxPrice(DEFAULT_MAX_AMOUNT)
                .withinRange(withinRange)
                .build();
    }
}