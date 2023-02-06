package com.zdz.service;

import com.zdz.domain.ResponseResult;
import com.zdz.domain.dto.AddRoleDto;
import com.zdz.domain.dto.RoleChangeStatusDto;
import com.zdz.domain.dto.RoleListDto;
import com.zdz.domain.dto.UpdateRoleDto;
import com.zdz.domain.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zdz.domain.vo.PageVo;
import com.zdz.domain.vo.RoleListVo;

import java.util.List;

/**
* @author zdz
* @description 针对表【sys_role(角色信息表)】的数据库操作Service
* @createDate 2023-01-31 13:07:24
*/
public interface RoleService extends IService<Role> {

    List<String> selectRoleKeyByUserId(Long userId);

    ResponseResult<PageVo> getRoleList(Integer pageNum, Integer pageSize, RoleListDto roleListDto);

    ResponseResult<?> changeStatus(RoleChangeStatusDto changeStatusDto);

    ResponseResult<?> addRole(AddRoleDto addRoleDto);

    ResponseResult<?> updateRole(UpdateRoleDto updateRoleDto);

    ResponseResult<List<RoleListVo>> listAllRole();
}
