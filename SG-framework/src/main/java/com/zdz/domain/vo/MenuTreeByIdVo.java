package com.zdz.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuTreeByIdVo {
    private List<MenuTreeVo> menus;
    private List<Long> checkedKeys;
}
