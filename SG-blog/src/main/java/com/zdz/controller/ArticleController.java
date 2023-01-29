package com.zdz.controller;

import com.zdz.domain.ResponseResult;
import com.zdz.domain.vo.ArticleDetailsVo;
import com.zdz.domain.vo.HotArticleVo;
import com.zdz.domain.vo.PageVo;
import com.zdz.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/article")
@Validated
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @GetMapping("/hotArticleList")
    public ResponseResult<List<HotArticleVo>> hotArticleList(){
        return articleService.hotArticleList();
    }
    
    @GetMapping("/articleList")
    public ResponseResult<PageVo> getArticleList(@RequestParam @NotNull(message = "页码不可为空") Integer pageNum, @RequestParam @NotNull(message = "分页大小不可为空") Integer pageSize, @RequestParam(required = false)Long categoryId){
        return articleService.getArticleList(pageNum, pageSize, categoryId);
    }


    @GetMapping("/articleDetails/{id}")
    public ResponseResult<ArticleDetailsVo> getArticleDetails(@PathVariable("id")Long id){
        return articleService.getArticleDetails(id);
    }

    @PutMapping("/updateViewCount/{id}")
    public ResponseResult<?> updateViewCount(@PathVariable("id") Long id){
        return articleService.updateViewCount(id);
    }
}
