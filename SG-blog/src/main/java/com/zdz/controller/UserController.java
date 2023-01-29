package com.zdz.controller;

import com.zdz.domain.ResponseResult;
import com.zdz.domain.dto.LoginDto;
import com.zdz.domain.dto.RegisterDto;
import com.zdz.domain.dto.UserInfoDto;
import com.zdz.domain.vo.LoginVo;
import com.zdz.domain.vo.UserInfoVo;
import com.zdz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseResult<LoginVo> login(@RequestBody @Validated LoginDto loginDto){
        return userService.login(loginDto);
    }

    @PostMapping("/logout")
    public ResponseResult logout(){
        return userService.logout();
    }

    @GetMapping("/userInfo")
    public ResponseResult<UserInfoVo> getUserInfo(){
        return userService.userInfo();
    }

    @PutMapping("/userInfo")
    public ResponseResult<?> updateUserInfo(@RequestBody @Validated UserInfoDto userInfoDto){
        return userService.updateUserInfo(userInfoDto);
    }

    @PostMapping("/register")
    public ResponseResult<?> register(@RequestBody @Validated RegisterDto registerDto){
        return userService.register(registerDto);
    }
}
