package com.zdz.domain.vo;

import com.zdz.domain.entity.LoginUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginVo {
    private String token;
    private UserInfoVo userInfo;
}
