package com.zdz.service;

import com.zdz.domain.ResponseResult;
import com.zdz.domain.dto.*;
import com.zdz.domain.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zdz.domain.vo.LoginVo;
import com.zdz.domain.vo.PageVo;
import com.zdz.domain.vo.SysUserByIdVo;
import com.zdz.domain.vo.UserInfoVo;

/**
* @author zdz
* @description 针对表【sys_user(用户表)】的数据库操作Service
* @createDate 2023-01-12 15:49:24
*/
public interface UserService extends IService<User> {

    ResponseResult<LoginVo> login(LoginDto loginDto);

    ResponseResult<?> logout();

    ResponseResult<UserInfoVo> userInfo();

    ResponseResult<?> updateUserInfo(UserInfoDto userInfoDto);

    ResponseResult<?> register(RegisterDto registerDto);

    ResponseResult<?> systemLogin(LoginDto loginDto);

    ResponseResult<?> systemLogout();

    ResponseResult<PageVo> getUserList(Integer pageNum, Integer pageSize, UserListDto userListDto);

    ResponseResult<?> addUser(AddUserDto addUserDto);

    ResponseResult<SysUserByIdVo> getUserById(Long id);

    ResponseResult<?> updateUser(UpdateUserDto updateUserDto);
}
