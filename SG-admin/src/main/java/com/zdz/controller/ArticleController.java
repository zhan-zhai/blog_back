package com.zdz.controller;

import com.zdz.domain.ResponseResult;
import com.zdz.domain.dto.AddArticleDto;
import com.zdz.service.ArticleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/content/article")
@Api(tags = "博文管理")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @PostMapping()
    @ApiOperation("新增博文")
    public ResponseResult<?> addArticle(@RequestBody @Validated AddArticleDto addArticleDto){
        return articleService.addArticle(addArticleDto);
    }
}
