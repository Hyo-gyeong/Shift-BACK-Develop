package com.project.shift.product.controller;

import com.project.shift.product.dto.PointCalcDTO;
import com.project.shift.product.dto.PointDTO;
import com.project.shift.product.service.IPointService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 금액권 관련 기능을 제공하는 컨트롤러
 * PROD-010 금액권 정보 조회
 * PROD-011 금액 입력 및 합계 계산
 * Endpoint: 
 * - GET /products/point
 * - POST /products/point/calc
 */
@RestController
@RequestMapping("/products")
public class PointController {

    private final IPointService pointService;

    public PointController(IPointService pointService) {
        this.pointService = pointService;
    }

    /**
     * [PROD-010]
     * 금액권 페이지 기본 정보 조회
     * - 대표 이미지, 설명, 최소/최대 금액 반환
     */
    @GetMapping("/point")
    public ResponseEntity<PointDTO> getPointPageInfo() {
        PointDTO pointInfo = pointService.getPointPageInfo();
        return ResponseEntity.ok(pointInfo);
    }
    
    /**
     * [PROD-011]
     * 금액 입력 및 합계 계산
     * - 사용자가 입력한 금액(inputAmount)과
     *   버튼 클릭으로 추가된 금액(addAmount)을 합산하고
     *   금액권의 허용 범위 내 유효성 검증을 수행한다.
     * - DB 갱신 없음 (조회용 API)
     * - 결제 및 포인트 적립은 SHOP-016, SHOP-017에서 처리
     */
    @PostMapping("/point/calc")
    public ResponseEntity<PointCalcDTO> calculatePoint(@RequestBody PointCalcDTO request) {
        PointCalcDTO result = pointService.calculatePoint(request);
        return ResponseEntity.ok(result);
    }
}