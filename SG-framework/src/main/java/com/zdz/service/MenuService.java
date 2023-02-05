package com.zdz.service;

import com.zdz.domain.ResponseResult;
import com.zdz.domain.dto.AddMenuDto;
import com.zdz.domain.dto.MenuListDto;
import com.zdz.domain.dto.UpdateMenuDto;
import com.zdz.domain.entity.Menu;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zdz.domain.vo.MenuListVo;
import com.zdz.domain.vo.MenuTreeByIdVo;
import com.zdz.domain.vo.MenuTreeVo;
import com.zdz.domain.vo.RoutersVo;

import java.util.List;

/**
* @author zdz
* @description 针对表【sys_menu(菜单权限表)】的数据库操作Service
* @createDate 2023-01-09 22:40:14
*/
public interface MenuService extends IService<Menu> {

    List<String> selectPermsByUserId(Long userId);

    ResponseResult<RoutersVo> getRouters();

    ResponseResult<List<MenuListVo>> menuList(MenuListDto menuListDto);

    ResponseResult<?> addMenu(AddMenuDto addMenuDto);

    ResponseResult<?> updateMenu(UpdateMenuDto updateMenuDto);

    ResponseResult<?> deleteMenu(Long id);

    ResponseResult<List<MenuTreeVo>> menuTreeSelect();

    ResponseResult<MenuTreeByIdVo> menuTreeSelectById(Long roleId);
}
