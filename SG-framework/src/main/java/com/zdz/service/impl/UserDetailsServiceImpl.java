package com.zdz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zdz.constants.CommonConstants;
import com.zdz.domain.entity.LoginUser;
import com.zdz.domain.entity.User;
import com.zdz.mapper.MenuMapper;
import com.zdz.mapper.UserMapper;
import com.zdz.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MenuService menuService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName,username);
        User user = userMapper.selectOne(queryWrapper);
        if(Objects.isNull(user)){
            throw new RuntimeException("用户不存在");
        }

        List<String> perms = null;
        if(user.getType().equals(CommonConstants.USER_TYPE_ADMIN)){

            perms = menuService.selectPermsByUserId(user.getId());
        }
        return new LoginUser(user,perms);
    }
}
