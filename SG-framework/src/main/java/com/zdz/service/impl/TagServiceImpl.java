package com.zdz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zdz.domain.ResponseResult;
import com.zdz.domain.dto.AddTagDto;
import com.zdz.domain.dto.TagListDto;
import com.zdz.domain.dto.UpdateTagDto;
import com.zdz.domain.entity.Tag;
import com.zdz.domain.vo.PageVo;
import com.zdz.domain.vo.TagVo;
import com.zdz.enums.AppHttpCodeEnum;
import com.zdz.exception.SystemException;
import com.zdz.service.TagService;
import com.zdz.mapper.TagMapper;
import com.zdz.utils.BeanCopyPropertiesUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
* @author zdz
* @description 针对表【sg_tag(标签)】的数据库操作Service实现
* @createDate 2023-01-09 22:35:49
*/
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag>
    implements TagService{

    @Override
    public ResponseResult<PageVo> tagList(Integer pageNum, Integer pageSize, TagListDto tagListDto) {
        LambdaQueryWrapper<Tag> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasText(tagListDto.getName()),Tag::getName,tagListDto.getName());
        wrapper.eq(StringUtils.hasText(tagListDto.getRemark()),Tag::getRemark,tagListDto.getRemark());
        Page<Tag> page = new Page<>(pageNum,pageSize);
        page(page,wrapper);
        List<Tag> tagList = page.getRecords();
        List<TagVo> tagVoList = BeanCopyPropertiesUtils.copyBeanList(tagList,TagVo.class);
        PageVo pageVo = new PageVo(tagVoList,page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult<?> addTag(AddTagDto addTagDto) {
        LambdaQueryWrapper<Tag> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Tag::getName,addTagDto.getName());
        Tag tag = getOne(wrapper);
        if(tag != null)throw new SystemException(AppHttpCodeEnum.TAG_IS_EXIST);
        Tag newTag = BeanCopyPropertiesUtils.copyBean(addTagDto,Tag.class);
        save(newTag);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<TagVo> getTagById(Long id) {
        Tag tag = getById(id);
        if(tag == null)throw new SystemException(AppHttpCodeEnum.TAG_NOT_EXIST);

        return ResponseResult.okResult(BeanCopyPropertiesUtils.copyBean(tag,TagVo.class));
    }

    @Override
    public ResponseResult<?> updateTag(UpdateTagDto updateTagDto) {
        LambdaUpdateWrapper<Tag> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Tag::getId,updateTagDto.getId());
        wrapper.set(Tag::getName,updateTagDto.getName());
        wrapper.set(Tag::getRemark,updateTagDto.getRemark());
        boolean update = update(new Tag(), wrapper);
        if(!update)
            return ResponseResult.errorResult(AppHttpCodeEnum.UPDATE_FAILED);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<List<TagVo>> listAllTag() {
        List<Tag> tagList = list();
        List<TagVo> tagVoList = BeanCopyPropertiesUtils.copyBeanList(tagList,TagVo.class);
        return ResponseResult.okResult(tagVoList);
    }


}




