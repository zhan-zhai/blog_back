package com.zdz.service;

import com.zdz.domain.ResponseResult;
import com.zdz.domain.dto.AddArticleDto;
import com.zdz.domain.dto.ArticleListDto;
import com.zdz.domain.dto.UpdateArticleDto;
import com.zdz.domain.entity.Article;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zdz.domain.vo.ArticleDetailsVo;
import com.zdz.domain.vo.HotArticleVo;
import com.zdz.domain.vo.PageVo;
import com.zdz.domain.vo.SysArticleDetailsVo;

import java.util.List;

/**
* @author zdz
* @description 针对表【sg_article(文章表)】的数据库操作Service
* @createDate 2023-01-09 22:35:49
*/
public interface ArticleService extends IService<Article> {
    ResponseResult<List<HotArticleVo>> hotArticleList();

    ResponseResult<PageVo> getArticleList(Integer pageNum, Integer pageSize, Long categoryId);

    ResponseResult<ArticleDetailsVo> getArticleDetails(Long id);

    ResponseResult<?> updateViewCount(Long id);

    ResponseResult<?> addArticle(AddArticleDto addArticleDto);

    ResponseResult<PageVo> sysArticleList(Integer pageNum, Integer pageSize, ArticleListDto articleListDto);

    ResponseResult<SysArticleDetailsVo> getSysArticleDetails(Long id);

    ResponseResult<?> updateArticle(UpdateArticleDto articleDto);
}
