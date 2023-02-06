package com.zdz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zdz.domain.entity.UserRole;
import com.zdz.service.UserRoleService;
import com.zdz.mapper.UserRoleMapper;
import org.springframework.stereotype.Service;

/**
* @author zdz
* @description 针对表【sys_user_role(用户和角色关联表)】的数据库操作Service实现
* @createDate 2023-02-04 23:11:03
*/
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole>
    implements UserRoleService{

}




