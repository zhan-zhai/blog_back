package com.zdz.service;

import com.zdz.domain.ResponseResult;
import com.zdz.domain.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zdz.domain.vo.CategoryVo;

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
}
