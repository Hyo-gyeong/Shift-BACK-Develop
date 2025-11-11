package com.project.shift.product.service;

import com.project.shift.product.dto.PointCalcDTO;
import com.project.shift.product.dto.PointDTO;

/**
 * 금액권 관련 서비스 인터페이스
 * - PROD-010 : 금액권 페이지 정보 조회
 * - PROD-011 : 금액 입력 및 합계 계산
 */
public interface IPointService {
    
	/**
     * [PROD-010]
     * 금액권 페이지 진입 시 기본 정보 조회
     * (대표 이미지, 설명, 최소/최대 금액)
     */
    PointDTO getPointPageInfo();

    /**
     * [PROD-011]
     * 금액 입력 및 합계 계산
     * 사용자가 입력한 금액(inputAmount)과 추가 금액(addAmount)을 합산하고,
     * 금액권 허용 범위 내 유효성을 검증한다.
     * DB 반영 없음 (조회 전용)
     */
    PointCalcDTO calculatePoint(PointCalcDTO request);
    
}