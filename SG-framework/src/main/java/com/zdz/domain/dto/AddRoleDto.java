package com.zdz.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddRoleDto {
    private List<Long> menuIds;

    private String roleName;

    private String roleKey;

    private Integer roleSort;

    private String status;

    private String remark;
}
