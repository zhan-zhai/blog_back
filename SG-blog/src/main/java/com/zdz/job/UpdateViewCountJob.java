package com.zdz.job;

import com.zdz.constants.RedisConstants;
import com.zdz.domain.entity.Article;
import com.zdz.service.ArticleService;
import com.zdz.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UpdateViewCountJob {
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private ArticleService articleService;

    @Scheduled(cron = "0/30 * * * * ?")
    public void updateViewCount(){
        Map<String, Integer> cacheMap = redisCache.getCacheMap(RedisConstants.ARTICLE_VIEW_COUNT);
        List<Article> articles = cacheMap.entrySet().stream()
                .map(entry -> new Article(Long.valueOf(entry.getKey()),entry.getValue().longValue()))
                .collect(Collectors.toList());
        articleService.updateBatchById(articles);
    }
}
