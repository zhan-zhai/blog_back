package com.zdz.controller;

import com.zdz.domain.ResponseResult;
import com.zdz.domain.dto.AddMenuDto;
import com.zdz.domain.dto.MenuListDto;
import com.zdz.domain.dto.UpdateMenuDto;
import com.zdz.domain.vo.MenuListVo;
import com.zdz.domain.vo.MenuTreeByIdVo;
import com.zdz.domain.vo.MenuTreeVo;
import com.zdz.domain.vo.MenuVo;
import com.zdz.service.MenuService;
import com.zdz.utils.BeanCopyPropertiesUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/menu")
@Api("菜单管理")
@Validated
public class MenuController {
    @Autowired
    private MenuService menuService;

    @GetMapping("/list")
    @ApiOperation("获取菜单列表")
    public ResponseResult<List<MenuListVo>> menuList(MenuListDto menuListDto){
        return menuService.menuList(menuListDto);
    }

    @PostMapping()
    @ApiOperation("新增菜单" )
    public ResponseResult<?> addMenu(@RequestBody @Validated AddMenuDto addMenuDto){
        return menuService.addMenu(addMenuDto);
    }

    @GetMapping("/{id}")
    @ApiOperation("根据id查询菜单")
    public ResponseResult<MenuVo> getMenuById(@PathVariable @NotNull(message = "参数不可为空") Long id){
        MenuVo menuVo = BeanCopyPropertiesUtils.copyBean(menuService.getById(id),MenuVo.class);
        return ResponseResult.okResult(menuVo);
    }

    @PutMapping
    @ApiOperation("修改菜单")
    public ResponseResult<?> updateMenu(@RequestBody @Validated UpdateMenuDto updateMenuDto){
        return menuService.updateMenu(updateMenuDto);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除菜单")
    public ResponseResult<?> deleteMenu(@PathVariable @NotNull(message = "参数不可为空") Long id){
        return menuService.deleteMenu(id);
    }

    @GetMapping("/treeselect")
    @ApiOperation("查询菜单下拉树结构")
    public ResponseResult<List<MenuTreeVo>> menuTreeSelect(){
        return menuService.menuTreeSelect();
    }

    @GetMapping("/roleMenuTreeselect/{id}")
    @ApiOperation("根据角色ID查询菜单下拉树结构")
    public ResponseResult<MenuTreeByIdVo> menuTreeSelectById(@PathVariable @NotNull(message = "参数不可为空") Long id){
        return menuService.menuTreeSelectById(id);
    }
}
