package com.zdz.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysUserByIdVo {
    private List<Long> roleIds;
    private List<RoleListVo> roles;
    private UserListVo user;
}
