package com.project.shift.product.controller;

import com.project.shift.product.dto.PointCalcDTO;
import com.project.shift.product.dto.PointDTO;
import com.project.shift.product.service.IPointService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * [CTRL-002] 금액권(POINT) 관련 API 컨트롤러
 * ---------------------------------------------------------
 * - PROD-010 : 금액권 정보 조회
 * - PROD-011 : 금액 입력 및 합계 계산
 * - SHOP-016 : 금액권 주문 생성
 * - SHOP-017 : 금액권 결제 완료 (포인트 적립)
 * ---------------------------------------------------------
 * Endpoints:
 *  • GET  /products/point
 *  • POST /products/point/calc
 */
@RestController
@RequestMapping("/products/point")
public class PointController {

    private final IPointService pointService;

    public PointController(IPointService pointService) {
        this.pointService = pointService;
    }

    /** [PROD-010] 금액권 페이지 기본 정보 조회 */
    @GetMapping
    public ResponseEntity<PointDTO> getPointPageInfo() {
        return ResponseEntity.ok(pointService.getPointPageInfo());
    }

    /** [PROD-011] 금액 입력 및 합계 계산 */
    @PostMapping("/calc")
    public ResponseEntity<PointCalcDTO> calculatePoint(@RequestBody PointCalcDTO request) {
        return ResponseEntity.ok(pointService.calculatePoint(request));
    }
    
    /** [SHOP-016] 금액권 주문 생성 */
    
    /** [SHOP-017] 금액권 결제 완료 (포인트 적립) */
    
}