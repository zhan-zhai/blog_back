package com.zdz.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleDetailsVo {
    private Long id;

    private String roleName;

    private String roleKey;

    private Integer roleSort;

    private String status;

    private String remark;
}
