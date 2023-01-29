package com.zdz.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class RegisterDto {
    @NotBlank(message = "昵称不可为空")
    private String nickName;
    @NotBlank(message = "邮箱不可为空")
    private String email;
    @NotBlank(message = "密码不可为空")
    private String password;
    @NotBlank(message = "用户名不可为空")
    private String userName;
}
