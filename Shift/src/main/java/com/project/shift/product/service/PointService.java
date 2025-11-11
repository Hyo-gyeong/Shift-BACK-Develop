package com.project.shift.product.service;

import com.project.shift.product.dto.PointDTO;
import com.project.shift.product.repository.PointRepository;
import org.springframework.stereotype.Service;

/**
 * PROD-010 금액권 페이지 정보 조회 비즈니스 로직
 * ---------------------------------------
 * - DB의 category_id=3 대표 이미지만 조회.
 * - 나머지 값은 고정 템플릿 데이터로 구성.
 * - 실제 금액권 생성은 SHOP-016 (주문 생성)에서 처리됨.
 */
@Service
public class PointService implements IPointService {

    private final PointRepository pointRepository;

    public PointService(PointRepository pointRepository) {
        this.pointRepository = pointRepository;
    }

    @Override
    public PointDTO getPointPageInfo() {
        // ① DB에서 category_id=3 대표 이미지 조회
        String imageUrl = pointRepository.findPointImage();

        // ② 이미지 없을 경우 기본 배너 사용
        if (imageUrl == null) {
            imageUrl = "default_point_banner.jpg";
        }

        // ③ 고정 템플릿 데이터 구성
        return new PointDTO(
                "포인트 금액권",
                imageUrl,
                "선물처럼 포인트를 보내보세요.",
                1000,
                5000000
        );
    }
}