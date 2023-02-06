package com.zdz.mapper;

import com.zdz.domain.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author zdz
* @description 针对表【sys_role(角色信息表)】的数据库操作Mapper
* @createDate 2023-01-31 13:07:24
* @Entity com.zdz.domain.entity.Role
*/
public interface RoleMapper extends BaseMapper<Role> {

    List<String> selectRoleKeyByUserId(Long userId);
}




