package com.zdz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zdz.constants.CommonConstants;
import com.zdz.constants.RedisConstants;
import com.zdz.domain.ResponseResult;
import com.zdz.domain.dto.AddArticleDto;
import com.zdz.domain.dto.ArticleListDto;
import com.zdz.domain.dto.UpdateArticleDto;
import com.zdz.domain.entity.Article;
import com.zdz.domain.entity.ArticleTag;
import com.zdz.domain.vo.*;
import com.zdz.enums.AppHttpCodeEnum;
import com.zdz.mapper.CategoryMapper;
import com.zdz.service.ArticleService;
import com.zdz.mapper.ArticleMapper;
import com.zdz.service.ArticleTagService;
import com.zdz.utils.BeanCopyPropertiesUtils;
import com.zdz.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

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
    private CategoryMapper categoryMapper;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private ArticleTagService articleTagService;

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
        hotArticleVos = hotArticleVos.stream()
                .map(hotArticleVo -> hotArticleVo.setViewCount(Long.valueOf((Integer)redisCache.getCacheMapValue(RedisConstants.ARTICLE_VIEW_COUNT,hotArticleVo.getId().toString()))))
                .collect(Collectors.toList());
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
                .peek(articleListVo -> {
                    articleListVo.setCategoryName(categoryMapper.selectById(articleListVo.getCategoryId()).getName());
                    articleListVo.setViewCount(Long.valueOf((Integer)redisCache.getCacheMapValue(RedisConstants.ARTICLE_VIEW_COUNT,articleListVo.getId().toString())));
                })
                .collect(Collectors.toList());
        PageVo pageVo = new PageVo(articleListVos,page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult<ArticleDetailsVo> getArticleDetails(Long id) {
        Article article = getById(id);
        ArticleDetailsVo articleDetailsVo = BeanCopyPropertiesUtils.copyBean(article, ArticleDetailsVo.class);
        articleDetailsVo.setCategoryName(categoryMapper.selectById(article.getCategoryId()).getName());
        articleDetailsVo.setViewCount(Long.valueOf((Integer)redisCache.getCacheMapValue(RedisConstants.ARTICLE_VIEW_COUNT,id.toString())));
        return ResponseResult.okResult(articleDetailsVo);
    }

    @Override
    public ResponseResult<?> updateViewCount(Long id) {
        redisCache.incrementCacheMapValue(RedisConstants.ARTICLE_VIEW_COUNT,id.toString(),1);
        return ResponseResult.okResult();
    }

    @Override
    @Transactional
    public ResponseResult<?> addArticle(AddArticleDto addArticleDto) {
        Article article = BeanCopyPropertiesUtils.copyBean(addArticleDto,Article.class);
        save(article);
        List<ArticleTag> articleTags = addArticleDto.getTags().stream()
                .map(tagId->new ArticleTag(article.getId(),tagId))
                .collect(Collectors.toList());
        articleTagService.saveBatch(articleTags);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<PageVo> sysArticleList(Integer pageNum, Integer pageSize, ArticleListDto articleListDto) {
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(articleListDto.getTitle()),Article::getTitle,articleListDto.getTitle());
        wrapper.like(StringUtils.hasText(articleListDto.getSummary()),Article::getSummary,articleListDto.getSummary());
        Page<Article> page = new Page<>(pageNum,pageSize);
        page(page,wrapper);
        List<Article> articles = page.getRecords();
        List<SysArticleListVo> voList = BeanCopyPropertiesUtils.copyBeanList(articles,SysArticleListVo.class);
//        voList = voList.stream()
//                .map(vo->vo.setViewCount(Long.valueOf((Integer)redisCache.getCacheMapValue(RedisConstants.ARTICLE_VIEW_COUNT,vo.getId().toString()))))
//                .collect(Collectors.toList());
        PageVo pageVo = new PageVo(voList,page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult<SysArticleDetailsVo> getSysArticleDetails(Long id) {
        Article article = getById(id);
        SysArticleDetailsVo articleDetailsVo = BeanCopyPropertiesUtils.copyBean(article,SysArticleDetailsVo.class);
//        articleDetailsVo.setViewCount(Long.valueOf((Integer)redisCache.getCacheMapValue(RedisConstants.ARTICLE_VIEW_COUNT,id.toString())));
        LambdaQueryWrapper<ArticleTag> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ArticleTag::getArticleId,id);
        List<ArticleTag> articleTags = articleTagService.list(wrapper);
        List<Long> tags = articleTags.stream().map(ArticleTag::getTagId).collect(Collectors.toList());
        articleDetailsVo.setTags(tags);
        return ResponseResult.okResult(articleDetailsVo);
    }

    @Override
    public ResponseResult<?> updateArticle(UpdateArticleDto articleDto) {
        LambdaUpdateWrapper<Article> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Article::getId,articleDto.getId());
        wrapper.set(Article::getTitle,articleDto.getTitle());
        wrapper.set(Article::getContent,articleDto.getContent());
        wrapper.set(Article::getSummary,articleDto.getSummary());
        wrapper.set(Article::getCategoryId,articleDto.getCategoryId());
        wrapper.set(Article::getThumbnail,articleDto.getThumbnail());
        wrapper.set(Article::getIsTop,articleDto.getIsTop());
        wrapper.set(Article::getStatus,articleDto.getStatus());
        wrapper.set(Article::getIsComment,articleDto.getIsComment());
        boolean update = update(new Article(),wrapper);
        if(!update)return ResponseResult.errorResult(AppHttpCodeEnum.UPDATE_FAILED);
        return ResponseResult.okResult();
    }
}




