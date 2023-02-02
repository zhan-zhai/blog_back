package com.zdz.domain.dto;

import lombok.Data;

import java.util.List;

@Data
public class AddArticleDto {

    private String title;

    private String content;

    private String summary;

    private Long categoryId;

    private String thumbnail;

    private String isTop;

    private String status;

    private String isComment;

    private List<Long> tags;
}
