package com.zdz.service;

import com.zdz.domain.ResponseResult;
import com.zdz.domain.entity.Menu;
import com.baomidou.mybatisplus.extension.service.IService;
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
}
