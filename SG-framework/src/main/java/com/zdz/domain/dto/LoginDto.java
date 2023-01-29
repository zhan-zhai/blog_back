package com.zdz.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginDto {
    @NotBlank(message = "用户名不可为空")
    private String userName;
    @NotBlank(message = "密码不可为空")
    private String password;
}
