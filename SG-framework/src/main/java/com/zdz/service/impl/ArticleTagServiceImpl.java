package com.zdz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zdz.domain.entity.ArticleTag;
import com.zdz.service.ArticleTagService;
import com.zdz.mapper.ArticleTagMapper;
import org.springframework.stereotype.Service;

/**
* @author zdz
* @description 针对表【sg_article_tag(文章标签关联表)】的数据库操作Service实现
* @createDate 2023-02-01 22:28:41
*/
@Service
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag>
    implements ArticleTagService{

}




