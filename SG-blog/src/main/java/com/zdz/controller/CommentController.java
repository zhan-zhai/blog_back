package com.zdz.controller;

import com.zdz.constants.CommonConstants;
import com.zdz.domain.ResponseResult;
import com.zdz.domain.dto.AddCommentDto;
import com.zdz.domain.vo.PageVo;
import com.zdz.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/comment")
@Validated
@Api(tags = "评论相关接口")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @GetMapping("/commentList")
    @ApiOperation("分页获取文章评论列表")
    public ResponseResult<PageVo> commentList(
            @RequestParam @NotNull(message = "文章id不可为空") Long articleId,
            @RequestParam @NotNull(message = "页码不可为空") Integer pageNum,
            @RequestParam @NotNull(message = "分页大小不可为空") Integer pageSize){
        return commentService.commentList(CommonConstants.COMMENT_TYPE_ARTICLE,articleId,pageNum,pageSize);
    }

    @PostMapping("/addComment")
    @ApiOperation("添加评论")
    public ResponseResult<?>addComment(@RequestBody @Validated AddCommentDto addCommentDto){
        return commentService.addComment(addCommentDto);
    }

    @GetMapping("/linkCommentList")
    @ApiOperation("分页获取友链评论")
    public ResponseResult<PageVo> linkCommentList(@RequestParam @NotNull(message = "页码不可为空") Integer pageNum,
                                             @RequestParam @NotNull(message = "分页大小不可为空") Integer pageSize){
        return commentService.commentList(CommonConstants.COMMENT_TYPE_LINK,null,pageNum,pageSize);
    }
}
