package com.zdz.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddTagDto {
    @NotBlank(message = "标签名不可为空")
    private String name;
    @NotBlank(message = "标签备注不可为空")
    private String remark;
}
