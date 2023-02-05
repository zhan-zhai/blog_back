package com.zdz.controller;

import com.zdz.constants.CommonConstants;
import com.zdz.domain.ResponseResult;
import com.zdz.domain.dto.AddCategoryDto;
import com.zdz.domain.dto.CategoryPageDto;
import com.zdz.domain.dto.UpdateCategoryDto;
import com.zdz.domain.vo.CategoryPageVo;
import com.zdz.domain.vo.CategoryVo;
import com.zdz.domain.vo.PageVo;
import com.zdz.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
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

    @GetMapping("/list")
    public ResponseResult<PageVo> categoryPageList(
            @RequestParam @NotNull(message = "页码不可为空") Integer pageNum,
            @RequestParam @NotNull(message = "分页大小不可为空") Integer pageSize,
            CategoryPageDto categoryPageDto){
        return categoryService.getCategoryPage(pageNum,pageSize,categoryPageDto);
    }

    @PostMapping
    public ResponseResult<?> addCategory(@RequestBody @Validated AddCategoryDto addCategoryDto){
        return categoryService.addCategory(addCategoryDto);
    }

    @GetMapping("/{id}")
    public ResponseResult<CategoryPageVo> getCategoryById(@PathVariable @NotNull(message = "参数不可为空") Long id){
        return categoryService.getCategoryById(id);
    }

    @PutMapping
    public ResponseResult<?> updateCategory(@RequestBody @Validated UpdateCategoryDto updateCategoryDto){
        return categoryService.updateCategory(updateCategoryDto);
    }

    @DeleteMapping("/{id}")
    public ResponseResult<?> deleteCategory(@PathVariable @NotNull(message = "参数不可为空") Long id){
        return ResponseResult.okResult(categoryService.removeById(id));
    }
}
