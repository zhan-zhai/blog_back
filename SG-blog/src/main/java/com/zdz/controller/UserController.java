package com.zdz.controller;

import com.zdz.domain.ResponseResult;
import com.zdz.domain.dto.LoginDto;
import com.zdz.domain.dto.RegisterDto;
import com.zdz.domain.dto.UserInfoDto;
import com.zdz.domain.vo.LoginVo;
import com.zdz.domain.vo.UserInfoVo;
import com.zdz.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Api(tags = "用户相关接口")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    @ApiOperation("用户登录")
    public ResponseResult<LoginVo> login(@RequestBody @Validated LoginDto loginDto){
        return userService.login(loginDto);
    }

    @PostMapping("/logout")
    @ApiOperation("用户登出")
    public ResponseResult<?> logout(){
        return userService.logout();
    }

    @GetMapping("/userInfo")
    @ApiOperation("获取用户信息")
    public ResponseResult<UserInfoVo> getUserInfo(){
        return userService.userInfo();
    }

    @PutMapping("/userInfo")
    @ApiOperation("更新用户信息")
    public ResponseResult<?> updateUserInfo(@RequestBody @Validated UserInfoDto userInfoDto){
        return userService.updateUserInfo(userInfoDto);
    }

    @PostMapping("/register")
    @ApiOperation("用户注册")
    public ResponseResult<?> register(@RequestBody @Validated RegisterDto registerDto){
        return userService.register(registerDto);
    }
}
