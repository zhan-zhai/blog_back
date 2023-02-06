package com.zdz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zdz.domain.entity.RoleMenu;
import com.zdz.service.RoleMenuService;
import com.zdz.mapper.RoleMenuMapper;
import org.springframework.stereotype.Service;

/**
* @author zdz
* @description 针对表【sys_role_menu(角色和菜单关联表)】的数据库操作Service实现
* @createDate 2023-02-03 23:36:21
*/
@Service
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu>
    implements RoleMenuService{

}




