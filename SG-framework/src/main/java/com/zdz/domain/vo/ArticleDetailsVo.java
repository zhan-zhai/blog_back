package com.zdz.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Date;

@Data
public class ArticleDetailsVo {
    private Long id;

    private String title;

    private String content;

    private String isComment;

    private String categoryName;

    private Long categoryId;

    private Long viewCount;

    private Date createTime;
}
