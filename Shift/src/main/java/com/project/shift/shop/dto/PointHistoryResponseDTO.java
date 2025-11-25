package com.project.shift.shop.dto;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PointHistoryResponseDTO {

    private Long userId;
    private Integer totalPoints;               // 현재 보유 포인트
    private List<PointHistoryDTO> history;     // A/U/R 내역 리스트
    private boolean result;                    // 성공 여부
}
