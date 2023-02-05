package com.zdz.controller;

import com.zdz.domain.ResponseResult;
import com.zdz.domain.dto.AddUserDto;
import com.zdz.domain.dto.LoginDto;
import com.zdz.domain.dto.UpdateUserDto;
import com.zdz.domain.dto.UserListDto;
import com.zdz.domain.entity.LoginUser;
import com.zdz.domain.vo.*;
import com.zdz.service.MenuService;
import com.zdz.service.RoleService;
import com.zdz.service.UserService;
import com.zdz.utils.BeanCopyPropertiesUtils;
import com.zdz.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@Api(tags = "后台系统用户相关接口")
@Validated
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private RoleService roleService;

    @PostMapping("/user/login")
    @ApiOperation("系统用户登录")
    public ResponseResult<?> login(@RequestBody @Validated LoginDto loginDto){
        return userService.systemLogin(loginDto);
    }

    @PostMapping("/user/logout")
    @ApiOperation("退出后台登录")
    public ResponseResult<?> logout(){
        return userService.systemLogout();
    }

    @GetMapping("/user/getInfo")
    @ApiOperation("获取系统用户信息")
    public ResponseResult<SystemUserInfoVo> getInfo(){
        //用户信息
        LoginUser loginUser = SecurityUtils.getLoginUser();
        UserInfoVo user = BeanCopyPropertiesUtils.copyBean(loginUser.getUser(),UserInfoVo.class);
        //用户全权限信息
        List<String> perms = menuService.selectPermsByUserId(user.getId());
        //用户角色信息
        List<String> roles = roleService.selectRoleKeyByUserId(user.getId());

        SystemUserInfoVo systemUserInfoVo =  new SystemUserInfoVo(perms,roles,user);
        return ResponseResult.okResult(systemUserInfoVo);
    }

    @GetMapping("/getRouters")
    @ApiOperation("获取系统用户路由信息")
    public ResponseResult<RoutersVo> getRouters(){
        return menuService.getRouters();
    }

    @GetMapping("/user/list")
    @ApiOperation("获取用户列表")
    public ResponseResult<PageVo> userList(@RequestParam @NotNull(message = "页码不可为空") Integer pageNum,
                                           @RequestParam @NotNull(message = "分页大小不可为空") Integer pageSize,
                                           UserListDto userListDto){
        return userService.getUserList(pageNum,pageSize,userListDto);
    }

    @PostMapping("/user")
    public ResponseResult<?> addUser(@RequestBody @Validated AddUserDto addUserDto){
        return userService.addUser(addUserDto);
    }

    @DeleteMapping("/user/{id}")
    public ResponseResult<?> deleteUser(@PathVariable @NotNull(message = "参数不可为空") Long id){
        return ResponseResult.okResult(userService.removeById(id));
    }

    @GetMapping("/user/{id}")
    public ResponseResult<SysUserByIdVo> getUserById(@PathVariable @NotNull(message = "参数不可为空") Long id){
        return userService.getUserById(id);
    }

    @PutMapping("/user")
    public ResponseResult<?> updateUser(@RequestBody @Validated UpdateUserDto updateUserDto){
        return userService.updateUser(updateUserDto);
    }
}
