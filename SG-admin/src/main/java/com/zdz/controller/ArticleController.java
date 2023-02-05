package com.zdz.controller;

import com.zdz.domain.ResponseResult;
import com.zdz.domain.dto.AddArticleDto;
import com.zdz.domain.dto.ArticleListDto;
import com.zdz.domain.dto.UpdateArticleDto;
import com.zdz.domain.vo.PageVo;
import com.zdz.domain.vo.SysArticleDetailsVo;
import com.zdz.domain.vo.SysArticleListVo;
import com.zdz.service.ArticleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/content/article")
@Api(tags = "博文管理")
@Validated
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @PostMapping()
    @ApiOperation("新增博文")
    public ResponseResult<?> addArticle(@RequestBody @Validated AddArticleDto addArticleDto){
        return articleService.addArticle(addArticleDto);
    }

    @GetMapping("/list")
    @ApiOperation("分页获取文章列表")
    public ResponseResult<PageVo> articleList(
            @RequestParam @NotNull(message = "页码不可为空") Integer pageNum,
            @RequestParam @NotNull(message = "分页大小不可为空") Integer pageSize,
            ArticleListDto articleListDto){
        return articleService.sysArticleList(pageNum,pageSize,articleListDto);
    }

    @GetMapping("/{id}")
    @ApiOperation("查询文章详情")
    public ResponseResult<SysArticleDetailsVo> getArticleById(@PathVariable @NotNull(message = "参数不可为空") Long id){
        return articleService.getSysArticleDetails(id);
    }

    @PutMapping()
    @ApiOperation("更新文章")
    public ResponseResult<?> updateArticle(@RequestBody @Validated UpdateArticleDto articleDto){
        return articleService.updateArticle(articleDto);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除文章")
    public ResponseResult<?> deleteArticle(@PathVariable @NotNull(message = "参数不可为空") Long id){
        return ResponseResult.okResult(articleService.removeById(id));
    }
}
