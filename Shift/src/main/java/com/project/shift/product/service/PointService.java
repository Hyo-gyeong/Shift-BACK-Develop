package com.project.shift.product.service;

import com.project.shift.product.dao.IPointDAO;
import com.project.shift.product.dto.PointCalcDTO;
import com.project.shift.product.dto.PointDTO;
import com.project.shift.product.entity.Product;
import com.project.shift.product.repository.PointRepository;
import org.springframework.stereotype.Service;

/**
 * 금액권 관련 서비스 구현 클래스
 * ---------------------------------------
 * PROD-010 : 금액권 페이지 정보 조회
 * PROD-011 : 금액 입력 및 합계 계산
 * ---------------------------------------
 * - DB의 category_id=3 상품을 금액권으로 간주.
 * - 실제 결제 및 포인트 적립은 SHOP-016, SHOP-017에서 처리.
 */
@Service
public class PointService implements IPointService {
	
	private static final int DEFAULT_MIN_AMOUNT = 1000;       // 금액권 최소 금액
    private static final int DEFAULT_MAX_AMOUNT = 5000000;    // 금액권 최대 금액

	private final IPointDAO pointDAO;
    private final PointRepository pointRepository;

    public PointService(PointRepository pointRepository, IPointDAO pointDAO) {
        this.pointRepository = pointRepository;
        this.pointDAO = pointDAO;
    }

    /**
     * [PROD-010]
     * 금액권 페이지 기본 정보 조회
     * - 대표 이미지, 설명, 최소/최대 금액 범위 반환
     */
    @Override
    public PointDTO getPointPageInfo() {
        String imageUrl = pointRepository.findPointImage();
        if (imageUrl == null) {
            imageUrl = "default_point_banner.jpg";
        }

        return new PointDTO(
                "포인트 금액권",
                imageUrl,
                "선물처럼 포인트를 보내보세요.",
                1000,
                5000000
        );
    }

    /**
     * [PROD-011]
     * 금액 입력 및 합계 계산
     * - 입력금액과 버튼 추가금액을 합산하고, 금액권 허용 범위를 검증한다.
     * - DB 갱신 없음, 조회 전용 API.
     */
    @Override
    public PointCalcDTO calculatePoint(PointCalcDTO request) {
    	// -------------------------------
        // [1] 입력값 검증
        // -------------------------------
        if (request == null) {
            throw new IllegalArgumentException("요청 데이터가 비어 있습니다.");
        }

        if (request.getInputAmount() < 0 || request.getAddAmount() < 0) {
            throw new IllegalArgumentException("금액은 0 이상이어야 합니다.");
        }

        // -------------------------------
        // [2] 금액 계산 로직
        // -------------------------------
        Product pointProduct = pointDAO.findPointTemplate();

        int totalAmount = request.getInputAmount() + request.getAddAmount();

        int minPrice = DEFAULT_MIN_AMOUNT;
        int maxPrice = DEFAULT_MAX_AMOUNT;

        boolean withinRange = (totalAmount >= minPrice && totalAmount <= maxPrice);

        // -------------------------------
        // [3] 응답 데이터 구성
        // -------------------------------
        PointCalcDTO result = new PointCalcDTO();
        result.setInputAmount(request.getInputAmount());
        result.setAddAmount(request.getAddAmount());
        result.setTotalAmount(totalAmount);
        result.setMinPrice(minPrice);
        result.setMaxPrice(maxPrice);
        result.setWithinRange(withinRange);

        return result;
    }
}