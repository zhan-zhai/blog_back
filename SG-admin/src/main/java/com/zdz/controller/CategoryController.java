package com.zdz.controller;

import com.zdz.constants.CommonConstants;
import com.zdz.domain.ResponseResult;
import com.zdz.domain.vo.CategoryVo;
import com.zdz.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/content/category")
@Api(tags = "分类管理")
@Validated
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/listAllCategory")
    @ApiOperation("获取所有分类列表")
    public ResponseResult<List<CategoryVo>> listAllCategory(){
        return categoryService.listAllCategory();
    }

    @GetMapping("/export")
    @ApiOperation("导出文件")
    @PreAuthorize("hasAuthority('content:category:export')")
    public void export(HttpServletResponse response){
        categoryService.exportExcel(response, CommonConstants.CATEGORY_EXPORT_FILE);
    }
}
