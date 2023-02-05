package com.zdz.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCategoryDto {
    private Long id;
    private String name;
    /**
     * 描述
     */
    private String description;

    /**
     * 状态0:正常,1禁用
     */
    private String status;
}
