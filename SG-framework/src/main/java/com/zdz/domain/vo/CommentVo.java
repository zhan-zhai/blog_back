package com.zdz.domain.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

@Data
@Accessors(chain = true)
public class CommentVo {
    private Long id;

    private Long articleId;

    private Long rootId;

    private String content;

    private Long toCommentUserId;

    private String toCommentUserName;

    private Long toCommentId;

    private Long createBy;

    private Date createTime;

    private String username;

    private List<CommentVo> children;
}
