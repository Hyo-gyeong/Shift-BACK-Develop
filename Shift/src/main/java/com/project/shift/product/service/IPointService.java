package com.project.shift.product.service;

import com.project.shift.product.dto.PointCalcDTO;
import com.project.shift.product.dto.PointDTO;

/**
 * [I-SERVICE-002] 금액권 관련 서비스 인터페이스
 * ---------------------------------------------------------
 * - PROD-010 : 금액권 페이지 기본 정보 조회
 * - PROD-011 : 금액 입력 및 합계 계산
 * ---------------------------------------------------------
 * ※ category_id = 3 인 상품을 금액권 템플릿으로 간주
 */
public interface IPointService {

    /** [PROD-010] 금액권 페이지 정보 조회 */
    PointDTO getPointPageInfo();

    /** [PROD-011] 금액 입력 및 합계 계산 */
    PointCalcDTO calculatePoint(PointCalcDTO request);
}