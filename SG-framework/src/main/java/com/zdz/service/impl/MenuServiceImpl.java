package com.zdz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zdz.constants.CommonConstants;
import com.zdz.domain.ResponseResult;
import com.zdz.domain.dto.AddMenuDto;
import com.zdz.domain.dto.MenuListDto;
import com.zdz.domain.dto.UpdateMenuDto;
import com.zdz.domain.entity.Menu;
import com.zdz.domain.entity.RoleMenu;
import com.zdz.domain.vo.*;
import com.zdz.enums.AppHttpCodeEnum;
import com.zdz.exception.SystemException;
import com.zdz.mapper.RoleMenuMapper;
import com.zdz.service.MenuService;
import com.zdz.mapper.MenuMapper;
import com.zdz.utils.BeanCopyPropertiesUtils;
import com.zdz.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
    @Autowired
    private RoleMenuMapper roleMenuMapper;

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

    @Override
    public ResponseResult<List<MenuListVo>> menuList(MenuListDto menuListDto) {
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(menuListDto.getStatus()),Menu::getStatus,menuListDto.getStatus());
        wrapper.like(StringUtils.hasText(menuListDto.getMenuName()),Menu::getMenuName,menuListDto.getMenuName());
        List<Menu> menuList = list(wrapper);
        List<MenuListVo> menuListVos = BeanCopyPropertiesUtils.copyBeanList(menuList,MenuListVo.class);
        return ResponseResult.okResult(menuListVos);
    }

    @Override
    public ResponseResult<?> addMenu(AddMenuDto addMenuDto) {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Menu::getMenuName,addMenuDto.getMenuName());
        Menu menu = getOne(queryWrapper);
        if(menu != null)throw new SystemException(AppHttpCodeEnum.ADD_MENU_FAIL);
        menu = BeanCopyPropertiesUtils.copyBean(addMenuDto,Menu.class);
        save(menu);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<?> updateMenu(UpdateMenuDto updateMenuDto) {
        if(updateMenuDto.getId().equals(updateMenuDto.getParentId()))
            throw new RuntimeException("修改菜单" + updateMenuDto.getMenuName() + "失败，上级菜单不能选择自己");
        LambdaUpdateWrapper<Menu> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Menu::getId,updateMenuDto.getId());
        wrapper.set(Menu::getMenuName,updateMenuDto.getMenuName());
        wrapper.set(Menu::getOrderNum,updateMenuDto.getOrderNum());
        wrapper.set(Menu::getMenuType,updateMenuDto.getMenuType());
        wrapper.set(Menu::getParentId,updateMenuDto.getParentId());
        wrapper.set(StringUtils.hasText(String.valueOf(updateMenuDto.getIsFrame())),Menu::getIsFrame,updateMenuDto.getIsFrame());
        wrapper.set(StringUtils.hasText(updateMenuDto.getIcon()),Menu::getIcon,updateMenuDto.getIcon());
        wrapper.set(StringUtils.hasText(updateMenuDto.getPath()),Menu::getPath,updateMenuDto.getPath());
        wrapper.set(StringUtils.hasText(updateMenuDto.getComponent()),Menu::getComponent,updateMenuDto.getComponent());
        wrapper.set(StringUtils.hasText(updateMenuDto.getPerms()),Menu::getPerms,updateMenuDto.getPerms());
        wrapper.set(StringUtils.hasText(updateMenuDto.getVisible()),Menu::getVisible,updateMenuDto.getVisible());
        wrapper.set(StringUtils.hasText(updateMenuDto.getStatus()),Menu::getStatus,updateMenuDto.getStatus());
        wrapper.set(StringUtils.hasText(updateMenuDto.getRemark()),Menu::getRemark,updateMenuDto.getRemark());
        boolean update = update(wrapper);
        if(!update)return ResponseResult.errorResult(AppHttpCodeEnum.UPDATE_FAILED);

        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<?> deleteMenu(Long id) {
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Menu::getParentId,id);
        List<Menu> list = list(wrapper);
        if(list.size() != 0)return ResponseResult.errorResult(AppHttpCodeEnum.DELETE_MENU_REFUSE);
        removeById(id);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<List<MenuTreeVo>> menuTreeSelect() {
//        List<Menu> menus;
//        if(roleId != null){
            LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Menu::getStatus,CommonConstants.MENU_STATUS_NORMAL);
            wrapper.orderByAsc(Menu::getParentId).orderByAsc(Menu::getOrderNum);
        List<Menu> menus = list(wrapper);
//        }
//        else menus = menuMapper.selectMenusByRoleId(roleId);
        List<MenuTreeVo> voList = menus.stream()
                .map(menu -> new MenuTreeVo(menu.getId(),menu.getMenuName(),menu.getParentId(),null))
                .collect(Collectors.toList());

        return ResponseResult.okResult(buildMenuTreeSelect(voList,0L));
    }

    @Override
    public ResponseResult<MenuTreeByIdVo> menuTreeSelectById(Long roleId) {
//        List<Menu> menus = menuMapper.selectMenusByRoleId(roleId);
//        List<MenuTreeVo> voList = menus.stream()
//                .map(menu -> new MenuTreeVo(menu.getId(),menu.getMenuName(),menu.getParentId(),null))
//                .collect(Collectors.toList());
        List<MenuTreeVo> voList =  menuTreeSelect().getData();
//        voList = buildMenuTreeSelect(voList,0L);
        LambdaUpdateWrapper<RoleMenu> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(RoleMenu::getRoleId,roleId);
        List<Long> menuIds = roleMenuMapper.selectList(wrapper).stream()
                .map(RoleMenu::getMenuId).collect(Collectors.toList());
        return ResponseResult.okResult(new MenuTreeByIdVo(voList,menuIds));
    }

    private List<MenuVo> buildMenuTree(List<MenuVo> menuVos,Long parentId) {
        return menuVos.stream()
                .filter(menuVo -> menuVo.getParentId().equals(parentId))
                .map(menuVo -> menuVo.setChildren(buildMenuTree(menuVos,menuVo.getId())))
                .collect(Collectors.toList());
    }

    private List<MenuTreeVo> buildMenuTreeSelect(List<MenuTreeVo> menuVos,Long parentId) {
        return menuVos.stream()
                .filter(menuVo -> menuVo.getParentId().equals(parentId))
                .map(menuVo -> menuVo.setChildren(buildMenuTreeSelect(menuVos,menuVo.getId())))
                .collect(Collectors.toList());
    }
}




