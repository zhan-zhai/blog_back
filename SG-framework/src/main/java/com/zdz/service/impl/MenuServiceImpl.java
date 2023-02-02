package com.zdz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zdz.constants.CommonConstants;
import com.zdz.domain.ResponseResult;
import com.zdz.domain.entity.Menu;
import com.zdz.domain.vo.MenuVo;
import com.zdz.domain.vo.RoutersVo;
import com.zdz.service.MenuService;
import com.zdz.mapper.MenuMapper;
import com.zdz.utils.BeanCopyPropertiesUtils;
import com.zdz.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
* @author zdz
* @description 针对表【sys_menu(菜单权限表)】的数据库操作Service实现
* @createDate 2023-01-09 22:40:14
*/
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu>
    implements MenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public List<String> selectPermsByUserId(Long userId) {
        //是超级管理员返回全部
        if(userId.equals(CommonConstants.SUPER_ADMIN)){
            LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
            wrapper.in(Menu::getMenuType, CommonConstants.MENU_TYPE_C,CommonConstants.MENU_TYPE_F);
            wrapper.eq(Menu::getStatus,CommonConstants.MENU_STATUS_NORMAL);
            List<Menu> menuList = list(wrapper);
            return menuList.stream()
                    .map(Menu::getPerms)
                    .collect(Collectors.toList());
        }

        return menuMapper.selectPermsByUserId(userId);
    }

    @Override
    public ResponseResult<RoutersVo> getRouters() {
        Long userId = SecurityUtils.getUserId();
        List<Menu> menus = null;
        if(userId.equals(CommonConstants.SUPER_ADMIN)){
            LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Menu::getStatus,CommonConstants.MENU_STATUS_NORMAL);
            wrapper.in(Menu::getMenuType,CommonConstants.MENU_TYPE_C,CommonConstants.MENU_TYPE_M);
            wrapper.orderByAsc(Menu::getParentId).orderByAsc(Menu::getOrderNum);
            menus = list(wrapper);
        }
        else menus = menuMapper.selectRoutersByUserId(userId);

        List<MenuVo> menuVos = menus.stream()
                .map(menu -> BeanCopyPropertiesUtils.copyBean(menu,MenuVo.class))
                .collect(Collectors.toList());
        RoutersVo routersVo = new RoutersVo(buildMenuTree(menuVos,0L));
        return ResponseResult.okResult(routersVo);
    }

    private List<MenuVo> buildMenuTree(List<MenuVo> menuVos,Long parentId) {
        return menuVos.stream()
                .filter(menuVo -> menuVo.getParentId().equals(parentId))
                .map(menuVo -> menuVo.setChildren(buildMenuTree(menuVos,menuVo.getId())))
                .collect(Collectors.toList());
    }
}




