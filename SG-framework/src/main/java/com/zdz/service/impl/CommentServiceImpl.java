package com.zdz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zdz.constants.CommonConstants;
import com.zdz.domain.ResponseResult;
import com.zdz.domain.dto.AddCommentDto;
import com.zdz.domain.entity.Comment;
import com.zdz.domain.vo.CommentVo;
import com.zdz.domain.vo.PageVo;
import com.zdz.service.CommentService;
import com.zdz.mapper.CommentMapper;
import com.zdz.service.UserService;
import com.zdz.utils.BeanCopyPropertiesUtils;
import com.zdz.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
* @author zdz
* @description 针对表【sg_comment(评论表)】的数据库操作Service实现
* @createDate 2023-01-09 22:35:49
*/
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
    implements CommentService{

    @Autowired
    private UserService userService;

    @Override
    public ResponseResult<PageVo> commentList(String type,Long articleId, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        if(type.equals(CommonConstants.COMMENT_TYPE_ARTICLE)){
            queryWrapper.eq(Comment::getArticleId,articleId);
            queryWrapper.eq(Comment::getRootId, CommonConstants.COMMENT_ROOT);
        }
        else queryWrapper.eq(Comment::getType,CommonConstants.COMMENT_TYPE_LINK);

        Page<Comment> page = new Page<>(pageNum,pageSize);
        page(page,queryWrapper);

        List<CommentVo> commentVos = toCommentVoList(page.getRecords());

        commentVos = commentVos.stream()
                .map(commentVo -> commentVo.setChildren(getChildren(commentVo.getId())))
                .collect(Collectors.toList());

        return ResponseResult.okResult(new PageVo(commentVos, page.getTotal()));
    }

    @Override
    public ResponseResult<?> addComment(AddCommentDto addCommentDto) {
        Comment comment = BeanCopyPropertiesUtils.copyBean(addCommentDto,Comment.class);
        save(comment);
        return ResponseResult.okResult();
    }

    private List<CommentVo> getChildren(Long id){
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getRootId,id);
        queryWrapper.orderByAsc(Comment::getCreateTime);
        List<Comment> comments = list(queryWrapper);

        return toCommentVoList(comments);
    }

    private List<CommentVo> toCommentVoList(List<Comment> comments){
        List<CommentVo> commentVos = BeanCopyPropertiesUtils.copyBeanList(comments, CommentVo.class);
        commentVos =  commentVos.stream()
                .map(commentVo -> commentVo.setUsername(userService.getById(commentVo.getCreateBy()).getNickName()))
                .peek(commentVo -> {
                    if(!commentVo.getToCommentUserId().equals(CommonConstants.COMMENT_ROOT))
                        commentVo.setToCommentUserName(userService.getById(commentVo.getToCommentUserId()).getUserName());
                })
                .collect(Collectors.toList());
        return commentVos;
    }
}




