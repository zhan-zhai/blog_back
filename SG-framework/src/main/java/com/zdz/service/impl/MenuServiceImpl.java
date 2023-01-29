package com.zdz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zdz.domain.entity.Menu;
import com.zdz.service.MenuService;
import com.zdz.mapper.MenuMapper;
import org.springframework.stereotype.Service;

/**
* @author zdz
* @description 针对表【sys_menu(菜单权限表)】的数据库操作Service实现
* @createDate 2023-01-09 22:40:14
*/
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu>
    implements MenuService {

}




