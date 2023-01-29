package com.zdz.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class AddCommentDto {
    @NotBlank(message = "评论类型不可为空")
    private String type;
    @NotNull(message = "文章id不可为空")
    private Long articleId;
    @NotNull(message = "rootId不可为空")
    private Long rootId;
    @NotBlank(message = "评论内容不可为空")
    private String content;
    @NotNull(message = "toCommentUserId不可为空")
    private Long toCommentUserId;
    @NotNull(message = "toCommentId不可为空")
    private Long toCommentId;

}
