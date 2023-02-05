package com.zdz.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class ArticleListVo {
    private Long id;

    private String title;

    private String summary;

    private String categoryName;

    @JsonIgnore
    private Long categoryId;

    private String thumbnail;

    private Long viewCount;

    private Date createTime;

}
