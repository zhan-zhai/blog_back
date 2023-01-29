package com.zdz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zdz.constants.CommonConstants;
import com.zdz.constants.RedisConstants;
import com.zdz.domain.ResponseResult;
import com.zdz.domain.dto.LoginDto;
import com.zdz.domain.dto.RegisterDto;
import com.zdz.domain.dto.UserInfoDto;
import com.zdz.domain.entity.LoginUser;
import com.zdz.domain.entity.User;
import com.zdz.domain.vo.LoginVo;
import com.zdz.domain.vo.UserInfoVo;
import com.zdz.enums.AppHttpCodeEnum;
import com.zdz.exception.SystemException;
import com.zdz.service.UserService;
import com.zdz.mapper.UserMapper;
import com.zdz.utils.BeanCopyPropertiesUtils;
import com.zdz.utils.JwtUtils;
import com.zdz.utils.RedisCache;
import com.zdz.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
* @author zdz
* @description 针对表【sys_user(用户表)】的数据库操作Service实现
* @createDate 2023-01-12 15:49:24
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public ResponseResult<LoginVo> login(LoginDto loginDto) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDto.getUserName(),loginDto.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        if(Objects.isNull(authenticate)){
            throw new RuntimeException("用户名或密码错误");
        }
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String token = JwtUtils.createJWT(userId);
        redisCache.setCacheObject(RedisConstants.BLOG_USER_LOGIN+userId,loginUser);
        UserInfoVo userInfoVo = BeanCopyPropertiesUtils.copyBean(loginUser.getUser(), UserInfoVo.class);
        LoginVo loginVo = new LoginVo(token,userInfoVo);
        return ResponseResult.okResult(loginVo);
    }

    @Override
    public ResponseResult logout() {
        Long userId = SecurityUtils.getUserId();
        redisCache.deleteObject(RedisConstants.BLOG_USER_LOGIN+userId);

        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<UserInfoVo> userInfo() {
        User user = SecurityUtils.getLoginUser().getUser();
        UserInfoVo userInfoVo = BeanCopyPropertiesUtils.copyBean(user,UserInfoVo.class);
        return ResponseResult.okResult(userInfoVo);
    }

    @Override
    public ResponseResult<?> updateUserInfo(UserInfoDto userInfoDto) {
        Long userId = userInfoDto.getId();
        if(!checkEmail(userInfoDto.getEmail(),userId))
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        if(!checkNickname(userInfoDto.getNickName(),userId))
            throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(User::getId,userId);
        updateWrapper.set(User::getAvatar,userInfoDto.getAvatar());
        updateWrapper.set(User::getNickName,userInfoDto.getNickName());
        updateWrapper.set(User::getEmail,userInfoDto.getEmail());
        updateWrapper.set(User::getSex,userInfoDto.getSex());
        /*
          使用boolean update(Wrapper<T> updateWrapper)，自动填充会失效
          当tableInfo为null时，是不走自动填充逻辑
          当实体对象为null时，则tableInfo 的值也是为null，这就会导致自动填充失效
          使用update(Wrapper<T> updateWrapper)的重载方法boolean update(T entity, Wrapper<T> updateWrapper)
         */
        boolean update = update(new User(),updateWrapper);
        if(!update)
            return ResponseResult.errorResult(AppHttpCodeEnum.UPDATE_FAILED);
        redisCache.deleteObject(RedisConstants.BLOG_USER_LOGIN+userId);
        User user = getById(userId);
        LoginUser loginUser = new LoginUser(user,null);
        redisCache.setCacheObject(RedisConstants.BLOG_USER_LOGIN+userId,loginUser);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<?> register(RegisterDto registerDto) {
        if(!checkUsername(registerDto.getUserName()))
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        if(!checkEmail(registerDto.getEmail(),null))
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        if(!checkNickname(registerDto.getNickName(),null))
            throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        User user = BeanCopyPropertiesUtils.copyBean(registerDto,User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        save(user);
        return ResponseResult.okResult();
    }

    private boolean checkUsername(String username){
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName,username);
        User user = getOne(queryWrapper);
        return Objects.isNull(user);
    }
    private boolean checkEmail(String email,Long userId){
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail,email);
        if(!Objects.isNull(userId))queryWrapper.ne(User::getId,userId);
        User user = getOne(queryWrapper);
        return Objects.isNull(user);
    }
    private boolean checkNickname(String nickname,Long userId){
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getNickName,nickname);
        if(!Objects.isNull(userId))queryWrapper.ne(User::getId,userId);
        User user = getOne(queryWrapper);
        return Objects.isNull(user);
    }
}




