package com.project.shift.global.validation.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InputValidationResponse {
    private boolean safe; // 검사 결과 안전 여부 (이슈가 없으면 true)
    private List<String> issues; // 발견된 위험 패턴 또는 의심스러운 항목 리스트
}