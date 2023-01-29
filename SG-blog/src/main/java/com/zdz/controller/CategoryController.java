package com.zdz.controller;

import com.zdz.domain.ResponseResult;
import com.zdz.domain.vo.CategoryVo;
import com.zdz.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/category")
@Api("文章分类相关接口")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/getCategoryList")
    @ApiOperation("获取文章分类列表")
    public ResponseResult<List<CategoryVo>> getCategoryList(){
        return categoryService.getCategoryList();
    }
}
