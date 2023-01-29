package com.zdz.controller;

import com.zdz.domain.ResponseResult;
import com.zdz.domain.vo.CategoryVo;
import com.zdz.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/getCategoryList")
    public ResponseResult<List<CategoryVo>> getCategoryList(){
        return categoryService.getCategoryList();
    }
}
