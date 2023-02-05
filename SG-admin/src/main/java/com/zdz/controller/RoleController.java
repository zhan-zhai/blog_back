package com.zdz.controller;

import com.zdz.domain.ResponseResult;
import com.zdz.domain.dto.AddRoleDto;
import com.zdz.domain.dto.RoleChangeStatusDto;
import com.zdz.domain.dto.RoleListDto;
import com.zdz.domain.dto.UpdateRoleDto;
import com.zdz.domain.vo.PageVo;
import com.zdz.domain.vo.RoleDetailsVo;
import com.zdz.domain.vo.RoleListVo;
import com.zdz.service.RoleService;
import com.zdz.utils.BeanCopyPropertiesUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/role")
@Validated
@Api("角色管理")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @GetMapping("/list")
    @ApiOperation("分页获取角色列表")
    public ResponseResult<PageVo> roleList(
            @RequestParam @NotNull(message = "页码不可为空") Integer pageNum,
            @RequestParam @NotNull(message = "分页大小不可为空") Integer pageSize,
            RoleListDto roleListDto){
        return roleService.getRoleList(pageNum,pageSize,roleListDto);
    }

    @PutMapping("/changeStatus")
    @ApiOperation("更改角色状态")
    public ResponseResult<?> changeRoleStatus(@RequestBody @Validated RoleChangeStatusDto changeStatusDto){
        return roleService.changeStatus(changeStatusDto);
    }

    @PostMapping()
    @ApiOperation("新增角色")
    public ResponseResult<?> addRole(@RequestBody @Validated AddRoleDto addRoleDto){
        return roleService.addRole(addRoleDto);
    }

    @GetMapping("/{id}")
    @ApiOperation("获取角色信息")
    public ResponseResult<RoleDetailsVo> getRoleById(@PathVariable @NotNull(message = "参数不可为空") Long id){
        RoleDetailsVo roleDetailsVo = BeanCopyPropertiesUtils.copyBean(roleService.getById(id),RoleDetailsVo.class);
        return ResponseResult.okResult(roleDetailsVo);
    }

    @PutMapping()
    public ResponseResult<?> updateRole(@RequestBody @Validated UpdateRoleDto updateRoleDto){
        return roleService.updateRole(updateRoleDto);
    }

    @DeleteMapping("/{id}")
    public ResponseResult<?> deleteRole(@PathVariable @NotNull(message = "参数不可为空") Long id){
        return ResponseResult.okResult(roleService.removeById(id));
    }

    @GetMapping("listAllRole")
    public ResponseResult<List<RoleListVo>> listAllRole(){
        return roleService.listAllRole();
    }
}
