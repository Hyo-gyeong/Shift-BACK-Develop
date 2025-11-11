package com.project.shift.product.dto;

import lombok.*;

/**
 * 카테고리 정보를 전달하는 DTO
 * - 카테고리 ID와 이름 포함
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDTO {
    private Long categoryId;    
    private String categoryName;
}