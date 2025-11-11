package com.project.shift.product.controller;

import com.project.shift.product.dto.PointDTO;
import com.project.shift.product.service.IPointService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * PROD-010 금액권 페이지 정보 조회 컨트롤러
 * Endpoint: GET /products/point
 */
@RestController
@RequestMapping("/products")
public class PointController {

    private final IPointService pointService;

    public PointController(IPointService pointService) {
        this.pointService = pointService;
    }

    /**
     * 금액권 페이지 기본 정보 조회
     * - 대표 이미지, 설명, 최소/최대 금액 반환
     */
    @GetMapping("/point")
    public ResponseEntity<PointDTO> getPointPageInfo() {
        PointDTO pointInfo = pointService.getPointPageInfo();
        return ResponseEntity.ok(pointInfo);
    }
}