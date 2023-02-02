package com.zdz.service;

import com.zdz.domain.ResponseResult;
import com.zdz.domain.dto.AddTagDto;
import com.zdz.domain.dto.TagListDto;
import com.zdz.domain.dto.UpdateTagDto;
import com.zdz.domain.entity.Tag;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zdz.domain.vo.PageVo;
import com.zdz.domain.vo.TagVo;

import java.util.List;

/**
* @author zdz
* @description 针对表【sg_tag(标签)】的数据库操作Service
* @createDate 2023-01-09 22:35:49
*/
public interface TagService extends IService<Tag> {

    ResponseResult<PageVo> tagList(Integer pageNum, Integer pageSize, TagListDto tagListDto);

    ResponseResult<?> addTag(AddTagDto addTagDto);

    ResponseResult<TagVo> getTagById(Long id);

    ResponseResult<?> updateTag(UpdateTagDto updateTagDto);

    ResponseResult<List<TagVo>> listAllTag();
}
