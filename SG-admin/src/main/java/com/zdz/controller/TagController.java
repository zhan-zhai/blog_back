package com.zdz.controller;

import com.zdz.domain.ResponseResult;
import com.zdz.domain.dto.AddTagDto;
import com.zdz.domain.dto.TagListDto;
import com.zdz.domain.dto.UpdateTagDto;
import com.zdz.domain.vo.PageVo;
import com.zdz.domain.vo.TagVo;
import com.zdz.service.TagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/content/tag")
@Api(tags = "标签管理")
@Validated
public class TagController {
    @Autowired
    private TagService tagService;

    @GetMapping("/list")
    @ApiOperation("分页获取标签")
    public ResponseResult<PageVo> list(
            @RequestParam @NotNull(message = "页码不可为空") Integer pageNum,
            @RequestParam @NotNull(message = "分页大小不可为空") Integer pageSize,
            TagListDto tagListDto){

        return tagService.tagList(pageNum,pageSize,tagListDto);
    }

    @PostMapping()
    @ApiOperation("添加标签")
    public ResponseResult<?> addTag(@RequestBody @Validated AddTagDto addTagDto){
        return tagService.addTag(addTagDto);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除标签")
    public ResponseResult<?> deleteTag(@PathVariable @NotNull(message = "参数不可为空") Long id){
        return ResponseResult.okResult(tagService.removeById(id));
    }

    @GetMapping("/{id}")
    @ApiOperation("获取标签信息")
    public ResponseResult<TagVo> getTag(@PathVariable @NotNull(message = "参数不可为空") Long id){
        return tagService.getTagById(id);
    }

    @PutMapping()
    @ApiOperation("修改标签")
    public ResponseResult<?> updateTag(@RequestBody @Validated UpdateTagDto updateTagDto){
        return tagService.updateTag(updateTagDto);
    }

    @GetMapping("/listAllTag")
    @ApiOperation("获取所有标签")
    public ResponseResult<List<TagVo>> listAllTag(){

        return tagService.listAllTag();
    }
}
