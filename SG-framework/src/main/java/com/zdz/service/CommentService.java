package com.zdz.service;

import com.zdz.domain.ResponseResult;
import com.zdz.domain.dto.AddCommentDto;
import com.zdz.domain.entity.Comment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zdz.domain.vo.PageVo;

/**
* @author zdz
* @description 针对表【sg_comment(评论表)】的数据库操作Service
* @createDate 2023-01-09 22:35:49
*/
public interface CommentService extends IService<Comment> {

    ResponseResult<PageVo> commentList(String type,Long articleId, Integer pageNum, Integer pageSize);

    ResponseResult<?> addComment(AddCommentDto addCommentDto);

//    ResponseResult<PageVo> linkCommentList(Integer pageNum, Integer pageSize);
}
