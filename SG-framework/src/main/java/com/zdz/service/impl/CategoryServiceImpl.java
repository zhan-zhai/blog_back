package com.zdz.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zdz.constants.CommonConstants;
import com.zdz.domain.ResponseResult;
import com.zdz.domain.dto.AddCategoryDto;
import com.zdz.domain.dto.CategoryPageDto;
import com.zdz.domain.dto.UpdateCategoryDto;
import com.zdz.domain.entity.Article;
import com.zdz.domain.entity.Category;
import com.zdz.domain.vo.CategoryExcelVo;
import com.zdz.domain.vo.CategoryPageVo;
import com.zdz.domain.vo.CategoryVo;
import com.zdz.domain.vo.PageVo;
import com.zdz.enums.AppHttpCodeEnum;
import com.zdz.exception.SystemException;
import com.zdz.mapper.ArticleMapper;
import com.zdz.service.CategoryService;
import com.zdz.mapper.CategoryMapper;
import com.zdz.utils.BeanCopyPropertiesUtils;
import com.zdz.utils.DownLoadExcelUtils;
import com.zdz.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
* @author zdz
* @description 针对表【sg_category(分类表)】的数据库操作Service实现
* @createDate 2023-01-09 21:36:56
*/
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
    implements CategoryService {

    @Autowired
    private ArticleMapper articleMapper;

    @Override
    /**
     * 展示分类列表，状态正常且发布了正式文章的分类
     */
    public ResponseResult<List<CategoryVo>> getCategoryList() {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getStatus, CommonConstants.ARTICLE_STATUS_PUBLISHED);
        List<Article> articles = articleMapper.selectList(queryWrapper);

        Set<Long> categoryIds = articles.stream()
                .map(Article::getCategoryId)
                .collect(Collectors.toSet());
        List<Category> categories = listByIds(categoryIds);
        categories = categories.stream()
                .filter(category -> category.getStatus().equals(CommonConstants.CATEGORY_STATUS_NORMAL))
                .collect(Collectors.toList());
        List<CategoryVo> categoryVos = BeanCopyPropertiesUtils.copyBeanList(categories,CategoryVo.class);
        return ResponseResult.okResult(categoryVos);
    }

    @Override
    public ResponseResult<List<CategoryVo>> listAllCategory() {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getStatus,CommonConstants.CATEGORY_STATUS_NORMAL);
        List<Category> categories = list(wrapper);
        List<CategoryVo> categoryVos = BeanCopyPropertiesUtils.copyBeanList(categories,CategoryVo.class);
        return ResponseResult.okResult(categoryVos);
    }

    @Override
    public void exportExcel(HttpServletResponse response, String fileName) {
        try {
            DownLoadExcelUtils.setDownLoadHeader(fileName,response);

            List<Category> categoryList = list();

            List<CategoryExcelVo> categoryExcelVos = BeanCopyPropertiesUtils.copyBeanList(categoryList,CategoryExcelVo.class);

            EasyExcel.write(response.getOutputStream(), CategoryExcelVo.class)
                    .autoCloseStream(Boolean.FALSE).sheet("文章分类")
                    .doWrite(categoryExcelVos);
        } catch (Exception e) {
            ResponseResult<?> result = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
            WebUtils.renderString(response, JSON.toJSONString(result));
        }

    }

    @Override
    public ResponseResult<PageVo> getCategoryPage(Integer pageNum, Integer pageSize, CategoryPageDto categoryPageDto) {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasText(categoryPageDto.getStatus()),Category::getStatus,categoryPageDto.getStatus());
        wrapper.like(StringUtils.hasText(categoryPageDto.getName()),Category::getName,categoryPageDto.getName());
        Page<Category> page = new Page<>(pageNum,pageSize);
        page(page,wrapper);
        List<Category> categories = page.getRecords();
        List<CategoryPageVo> voList = BeanCopyPropertiesUtils.copyBeanList(categories,CategoryPageVo.class);
        PageVo pageVo = new PageVo(voList,page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult<?> addCategory(AddCategoryDto addCategoryDto) {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getName,addCategoryDto.getName());
        Category one = getOne(wrapper);
        if(one!=null)throw new SystemException(AppHttpCodeEnum.CATEGORY_IS_EXIST);
        Category category = BeanCopyPropertiesUtils.copyBean(addCategoryDto,Category.class);
        save(category);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<CategoryPageVo> getCategoryById(Long id) {
        Category category = getById(id);
        CategoryPageVo categoryPageVo = BeanCopyPropertiesUtils.copyBean(category,CategoryPageVo.class);
        return ResponseResult.okResult(categoryPageVo);
    }

    @Override
    public ResponseResult<?> updateCategory(UpdateCategoryDto updateCategoryDto) {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getName,updateCategoryDto.getName());
        Category one = getOne(wrapper);
        if(one!=null)throw new SystemException(AppHttpCodeEnum.CATEGORY_IS_EXIST);
        LambdaUpdateWrapper<Category> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Category::getId,updateCategoryDto.getId());
        updateWrapper.set(Category::getName,updateCategoryDto.getName());
        updateWrapper.set(Category::getDescription,updateCategoryDto.getDescription());
        updateWrapper.set(Category::getStatus,updateCategoryDto.getStatus());
        update(new Category(),updateWrapper);
        return ResponseResult.okResult();
    }
}
