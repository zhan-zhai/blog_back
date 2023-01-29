package com.zdz.runner;

import com.zdz.constants.RedisConstants;
import com.zdz.domain.entity.Article;
import com.zdz.mapper.ArticleMapper;
import com.zdz.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ViewCountRunner implements CommandLineRunner {
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private RedisCache redisCache;

    @Override
    public void run(String... args) throws Exception {
        List<Article> articles = articleMapper.selectList(null);
        Map<String, Integer> map = articles.stream()
                .collect(Collectors.toMap(article -> article.getId().toString()
                        , article -> article.getViewCount().intValue()));
        redisCache.setCacheMap(RedisConstants.ARTICLE_VIEW_COUNT,map);
    }
}
