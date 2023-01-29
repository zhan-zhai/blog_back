package com.zdz.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UserInfoDto {
    @NotNull(message = "用户id不可为空")
    private Long id;
    @NotBlank(message = "昵称不可为空")
    private String nickName;
    @NotBlank(message = "邮箱不可为空")
    private String email;
    @NotBlank(message = "性别不可为空")
    private String sex;
    @NotBlank(message = "头像地址不可为空")
    private String avatar;

}
