package com.zdz.controller;

import com.zdz.constants.CommonConstants;
import com.zdz.domain.ResponseResult;
import com.zdz.domain.dto.AddCommentDto;
import com.zdz.domain.vo.PageVo;
import com.zdz.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/comment")
@Validated
public class CommentController {
    @Autowired
    private CommentService commentService;

    @GetMapping("/commentList")
    public ResponseResult<PageVo> commentList(
            @RequestParam @NotNull(message = "文章id不可为空") Long articleId,
            @RequestParam @NotNull(message = "页码不可为空") Integer pageNum,
            @RequestParam @NotNull(message = "分页大小不可为空") Integer pageSize){
        return commentService.commentList(CommonConstants.COMMENT_TYPE_ARTICLE,articleId,pageNum,pageSize);
    }

    @PostMapping("/addComment")
    public ResponseResult<?>addComment(@RequestBody @Validated AddCommentDto addCommentDto){
        return commentService.addComment(addCommentDto);
    }

    @GetMapping("/linkCommentList")
    public ResponseResult<PageVo> linkCommentList(@RequestParam @NotNull(message = "页码不可为空") Integer pageNum,
                                             @RequestParam @NotNull(message = "分页大小不可为空") Integer pageSize){
        return commentService.commentList(CommonConstants.COMMENT_TYPE_LINK,null,pageNum,pageSize);
    }
}
