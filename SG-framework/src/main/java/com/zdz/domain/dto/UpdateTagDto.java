package com.zdz.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UpdateTagDto {
    @NotNull(message = "id不可为空")
    private Long id;
    @NotBlank(message = "标签名不可为空")
    private String name;
    @NotBlank(message = "标签备注不可为空")
    private String remark;
}
