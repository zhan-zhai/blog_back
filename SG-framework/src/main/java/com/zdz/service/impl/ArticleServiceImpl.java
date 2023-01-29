package com.zdz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zdz.constants.CommonConstants;
import com.zdz.constants.RedisConstants;
import com.zdz.domain.ResponseResult;
import com.zdz.domain.entity.Article;
import com.zdz.domain.vo.ArticleDetailsVo;
import com.zdz.domain.vo.ArticleListVo;
import com.zdz.domain.vo.HotArticleVo;
import com.zdz.domain.vo.PageVo;
import com.zdz.mapper.CategoryMapper;
import com.zdz.service.ArticleService;
import com.zdz.mapper.ArticleMapper;
import com.zdz.utils.BeanCopyPropertiesUtils;
import com.zdz.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
* @author zdz
* @description 针对表【sg_article(文章表)】的数据库操作Service实现
* @createDate 2023-01-09 22:35:49
*/
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article>
    implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private RedisCache redisCache;

    @Override
    /**
     * 热门文章列表，已发布状态，前十条，按照访问量排序
     */
    public ResponseResult<List<HotArticleVo>> hotArticleList() {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getStatus, CommonConstants.ARTICLE_STATUS_PUBLISHED);
        queryWrapper.orderByDesc(Article::getViewCount);
        Page<Article> page = new Page<>(CommonConstants.FIRST_PAGE,CommonConstants.PAGE_SIZE);
        page(page,queryWrapper);
        List<Article> articles = page.getRecords();
        List<HotArticleVo> hotArticleVos = BeanCopyPropertiesUtils.copyBeanList(articles, HotArticleVo.class);
        return ResponseResult.okResult(hotArticleVos);
    }

    @Override
    /**
     * 文章列表展示，查询所有文章或对应分类下的文章，置顶在最前
     */
    public ResponseResult<PageVo> getArticleList(Integer pageNum, Integer pageSize, Long categoryId) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getStatus, CommonConstants.ARTICLE_STATUS_PUBLISHED);
        if(categoryId != null && categoryId > 0)queryWrapper.eq(Article::getCategoryId,categoryId);
        queryWrapper.orderByDesc(Article::getIsTop);
        Page<Article> page = new Page<>(pageNum,pageSize);
//        IPage<ArticleListVo> iPage = articleMapper.selectArticleListPage(page,queryWrapper);
        page(page,queryWrapper);
        List<Article> articles = page.getRecords();

        List<ArticleListVo> articleListVos = BeanCopyPropertiesUtils.copyBeanList(articles,ArticleListVo.class);
        articleListVos = articleListVos.stream()
                .map(articleListVo -> articleListVo.setCategoryName(categoryMapper.selectById(articleListVo.getCategoryId()).getName()))
                .collect(Collectors.toList());
        PageVo pageVo = new PageVo(articleListVos,page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult<ArticleDetailsVo> getArticleDetails(Long id) {
        Article article = getById(id);
        ArticleDetailsVo articleDetailsVo = BeanCopyPropertiesUtils.copyBean(article, ArticleDetailsVo.class);
        articleDetailsVo.setCategoryName(categoryMapper.selectById(article.getCategoryId()).getName());
        return ResponseResult.okResult(articleDetailsVo);
    }

    @Override
    public ResponseResult<?> updateViewCount(Long id) {
        redisCache.incrementCacheMapValue(RedisConstants.ARTICLE_VIEW_COUNT,id.toString(),1);
        return ResponseResult.okResult();
    }
}




