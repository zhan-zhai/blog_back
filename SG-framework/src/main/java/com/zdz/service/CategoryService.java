package com.zdz.service;

import com.zdz.domain.ResponseResult;
import com.zdz.domain.dto.AddCategoryDto;
import com.zdz.domain.dto.CategoryPageDto;
import com.zdz.domain.dto.UpdateCategoryDto;
import com.zdz.domain.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zdz.domain.vo.CategoryPageVo;
import com.zdz.domain.vo.CategoryVo;
import com.zdz.domain.vo.PageVo;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
* @author zdz
* @description 针对表【sg_category(分类表)】的数据库操作Service
* @createDate 2023-01-09 21:36:56
*/
public interface CategoryService extends IService<Category> {
    ResponseResult<List<CategoryVo>> getCategoryList();

    ResponseResult<List<CategoryVo>> listAllCategory();

    void exportExcel(HttpServletResponse response, String fileName);

    ResponseResult<PageVo> getCategoryPage(Integer pageNum, Integer pageSize, CategoryPageDto categoryPageDto);

    ResponseResult<?> addCategory(AddCategoryDto addCategoryDto);

    ResponseResult<CategoryPageVo> getCategoryById(Long id);

    ResponseResult<?> updateCategory(UpdateCategoryDto updateCategoryDto);
}
