package com.zdz.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zdz.domain.entity.Article;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zdz.domain.vo.ArticleListVo;
import org.springframework.data.repository.query.Param;

/**
* @author zdz
* @description 针对表【sg_article(文章表)】的数据库操作Mapper
* @createDate 2023-01-09 22:35:49
* @Entity com.zdz.framework.domain.entity.Article
*/
public interface ArticleMapper extends BaseMapper<Article> {
//    IPage<ArticleListVo> selectArticleListPage(Page<ArticleListVo> page, @Param(Constants.WRAPPER)Wrapper<Article> wrapper);
}




