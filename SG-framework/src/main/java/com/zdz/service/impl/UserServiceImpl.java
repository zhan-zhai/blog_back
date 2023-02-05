package com.zdz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zdz.constants.CommonConstants;
import com.zdz.constants.RedisConstants;
import com.zdz.domain.ResponseResult;
import com.zdz.domain.dto.*;
import com.zdz.domain.entity.*;
import com.zdz.domain.vo.*;
import com.zdz.enums.AppHttpCodeEnum;
import com.zdz.exception.SystemException;
import com.zdz.mapper.*;
import com.zdz.service.RoleService;
import com.zdz.service.UserRoleService;
import com.zdz.service.UserService;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private RoleService roleService;

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
    public ResponseResult<?> logout() {
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
        if(checkEmail(userInfoDto.getEmail(),userId))
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        if(checkNickname(userInfoDto.getNickName(),userId))
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
        if(checkUsername(registerDto.getUserName()))
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        if(checkEmail(registerDto.getEmail(),null))
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        if(checkNickname(registerDto.getNickName(),null))
            throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        User user = BeanCopyPropertiesUtils.copyBean(registerDto,User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        save(user);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<?> systemLogin(LoginDto loginDto) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDto.getUserName(),loginDto.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        if(Objects.isNull(authenticate)){
            throw new SystemException(AppHttpCodeEnum.LOGIN_ERROR);
        }
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        if(!loginUser.getUser().getType().equals(CommonConstants.USER_TYPE_ADMIN))
            throw new SystemException(AppHttpCodeEnum.NEED_ADMIN);
        String userId = loginUser.getUser().getId().toString();
        String token = JwtUtils.createJWT(userId);
        redisCache.setCacheObject(RedisConstants.BLOG_ADMIN_LOGIN+userId,loginUser);
        Map<String,String> map = new HashMap<>();
        map.put("token",token);
        return ResponseResult.okResult(map);
    }

    @Override
    public ResponseResult<?> systemLogout() {
        Long userId = SecurityUtils.getUserId();
        redisCache.deleteObject(RedisConstants.BLOG_ADMIN_LOGIN+userId);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<PageVo> getUserList(Integer pageNum, Integer pageSize, UserListDto userListDto) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(userListDto.getUserName()),User::getUserName,userListDto.getUserName());
        wrapper.eq(StringUtils.hasText(userListDto.getPhonenumber()),User::getPhonenumber,userListDto.getPhonenumber());
        wrapper.eq(StringUtils.hasText(userListDto.getStatus()),User::getStatus,userListDto.getStatus());
        Page<User> page = new Page<>(pageNum,pageSize);
        page(page,wrapper);
        List<User> userList = page.getRecords();
        List<UserListVo> voList = BeanCopyPropertiesUtils.copyBeanList(userList,UserListVo.class);
        PageVo pageVo = new PageVo(voList,page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    @Transactional
    public ResponseResult<?> addUser(AddUserDto addUserDto) {
        if(checkUsername(addUserDto.getUserName()))
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        if(checkEmail(addUserDto.getEmail(),null))
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        if(checkNickname(addUserDto.getNickName(),null))
            throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        User user = BeanCopyPropertiesUtils.copyBean(addUserDto,User.class);
        user.setType(CommonConstants.USER_TYPE_ADMIN);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        save(user);
        List<UserRole> userRoles = addUserDto.getRoleIds().stream()
                .map(roleId -> new UserRole(user.getId(),roleId))
                .collect(Collectors.toList());
        userRoleService.saveBatch(userRoles);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<SysUserByIdVo> getUserById(Long id) {
        User user = getById(id);
        UserListVo userInfoVo = BeanCopyPropertiesUtils.copyBean(user,UserListVo.class);
        List<RoleListVo> roles = roleService.listAllRole().getData();
        LambdaUpdateWrapper<UserRole> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(UserRole::getUserId,user.getId());
        List<Long> roleIds = userRoleService.list(wrapper).stream()
                .map(UserRole::getRoleId).collect(Collectors.toList());
        return ResponseResult.okResult(new SysUserByIdVo(roleIds,roles,userInfoVo));
    }

    @Override
    @Transactional
    public ResponseResult<?> updateUser(UpdateUserDto updateUserDto) {
        LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(User::getId,updateUserDto.getId());
        wrapper.set(User::getUserName,updateUserDto.getUserName());
        wrapper.set(User::getEmail,updateUserDto.getEmail());
        wrapper.set(User::getNickName,updateUserDto.getNickName());
        wrapper.set(User::getSex,updateUserDto.getSex());
        wrapper.set(User::getStatus,updateUserDto.getStatus());
        update(new User(),wrapper);
        LambdaQueryWrapper<UserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserRole::getUserId,updateUserDto.getId());
        userRoleService.remove(queryWrapper);
        List<UserRole> userRoles = updateUserDto.getRoleIds().stream()
                .map(roleId->new UserRole(updateUserDto.getId(),roleId))
                .collect(Collectors.toList());
        userRoleService.saveBatch(userRoles);
        return ResponseResult.okResult();
    }

    private boolean checkUsername(String username){
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName,username);
        User user = getOne(queryWrapper);
        return !Objects.isNull(user);
    }
    private boolean checkEmail(String email,Long userId){
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail,email);
        if(!Objects.isNull(userId))queryWrapper.ne(User::getId,userId);
        User user = getOne(queryWrapper);
        return !Objects.isNull(user);
    }
    private boolean checkNickname(String nickname,Long userId){
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getNickName,nickname);
        if(!Objects.isNull(userId))queryWrapper.ne(User::getId,userId);
        User user = getOne(queryWrapper);
        return !Objects.isNull(user);
    }
}




