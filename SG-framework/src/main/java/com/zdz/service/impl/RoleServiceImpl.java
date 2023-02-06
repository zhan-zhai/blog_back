package com.zdz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zdz.constants.CommonConstants;
import com.zdz.domain.ResponseResult;
import com.zdz.domain.dto.AddRoleDto;
import com.zdz.domain.dto.RoleChangeStatusDto;
import com.zdz.domain.dto.RoleListDto;
import com.zdz.domain.dto.UpdateRoleDto;
import com.zdz.domain.entity.Role;
import com.zdz.domain.entity.RoleMenu;
import com.zdz.domain.vo.PageVo;
import com.zdz.domain.vo.RoleListVo;
import com.zdz.enums.AppHttpCodeEnum;
import com.zdz.exception.SystemException;
import com.zdz.service.RoleMenuService;
import com.zdz.service.RoleService;
import com.zdz.mapper.RoleMapper;
import com.zdz.utils.BeanCopyPropertiesUtils;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author zdz
* @description 针对表【sys_role(角色信息表)】的数据库操作Service实现
* @createDate 2023-01-31 13:07:24
*/
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role>
    implements RoleService{

    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private RoleMenuService roleMenuService;

    @Override
    public List<String> selectRoleKeyByUserId(Long userId) {
        if(userId.equals(CommonConstants.SUPER_ADMIN)){
            List<String> roles = new ArrayList<>();
            roles.add("admin");
            return roles;
        }
        return roleMapper.selectRoleKeyByUserId(userId);
    }

    @Override
    public ResponseResult<PageVo> getRoleList(Integer pageNum, Integer pageSize, RoleListDto roleListDto) {
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(roleListDto.getRoleName()),Role::getRoleName,roleListDto.getRoleName());
        wrapper.eq(StringUtils.hasText(roleListDto.getStatus()),Role::getStatus,roleListDto.getStatus());
        wrapper.orderByAsc(Role::getRoleSort);
        Page<Role> page = new Page<>(pageNum,pageSize);
        page(page,wrapper);
        List<Role> roles = page.getRecords();
        List<RoleListVo> voList = BeanCopyPropertiesUtils.copyBeanList(roles,RoleListVo.class);
        PageVo pageVo = new PageVo(voList,page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult<?> changeStatus(RoleChangeStatusDto changeStatusDto) {
        LambdaUpdateWrapper<Role> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Role::getId,changeStatusDto.getRoleId());
        wrapper.set(Role::getStatus,changeStatusDto.getStatus());
        boolean update = update(new Role(), wrapper);
        if(!update)return ResponseResult.errorResult(AppHttpCodeEnum.UPDATE_FAILED);
        return ResponseResult.okResult();
    }

    @Override
    @Transactional
    public ResponseResult<?> addRole(AddRoleDto addRoleDto) {
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Role::getRoleName,addRoleDto.getRoleName());
        wrapper.or().eq(Role::getRoleKey,addRoleDto.getRoleKey());
        Role one = getOne(wrapper);
        if(one!=null)throw new SystemException(AppHttpCodeEnum.ROLE_INFO_EXIST);
        Role role = BeanCopyPropertiesUtils.copyBean(addRoleDto,Role.class);
        save(role);
        List<RoleMenu> roleMenus = addRoleDto.getMenuIds().stream()
                .map(id->new RoleMenu(role.getId(),id)).collect(Collectors.toList());
        roleMenuService.saveBatch(roleMenus);
        return ResponseResult.okResult();
    }

    @Override
    @Transactional
    public ResponseResult<?> updateRole(UpdateRoleDto updateRoleDto) {
        LambdaUpdateWrapper<Role> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Role::getId,updateRoleDto.getId());
        wrapper.set(Role::getRoleKey,updateRoleDto.getRoleKey());
        wrapper.set(Role::getRoleName,updateRoleDto.getRoleName());
        wrapper.set(Role::getRoleSort,updateRoleDto.getRoleSort());
        wrapper.set(Role::getRemark,updateRoleDto.getRemark());
        wrapper.set(Role::getStatus,updateRoleDto.getStatus());
        boolean update = update(new Role(), wrapper);
        if(!update)return ResponseResult.errorResult(AppHttpCodeEnum.UPDATE_FAILED);
        List<RoleMenu> roleMenus = updateRoleDto.getMenuIds().stream()
                .map(id->new RoleMenu(updateRoleDto.getId(),id)).collect(Collectors.toList());
        LambdaQueryWrapper<RoleMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleMenu::getRoleId,updateRoleDto.getId());
        roleMenuService.remove(queryWrapper);
        roleMenuService.saveBatch(roleMenus);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<List<RoleListVo>> listAllRole() {
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Role::getStatus,CommonConstants.ROLE_STATUS_NORMAL);
        List<Role> roles = list(wrapper);
        List<RoleListVo> voList = BeanCopyPropertiesUtils.copyBeanList(roles,RoleListVo.class);
        return ResponseResult.okResult(voList);
    }
}




