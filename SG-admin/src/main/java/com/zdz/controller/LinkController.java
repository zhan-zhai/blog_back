package com.zdz.controller;

import com.zdz.domain.ResponseResult;
import com.zdz.domain.dto.AddLinkDto;
import com.zdz.domain.dto.LinkPageDto;
import com.zdz.domain.dto.UpdateLinkDto;
import com.zdz.domain.vo.LinkPageVo;
import com.zdz.domain.vo.PageVo;
import com.zdz.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/content/link")
public class LinkController {
    @Autowired
    private LinkService linkService;

    @GetMapping("/list")
    public ResponseResult<PageVo> linkPageList(
            @RequestParam @NotNull(message = "页码不可为空") Integer pageNum,
            @RequestParam @NotNull(message = "分页大小不可为空") Integer pageSize,
            LinkPageDto linkPageDto){
        return linkService.linkPageList(pageNum,pageSize,linkPageDto);
    }

    @PostMapping
    public ResponseResult<?> addLink(@RequestBody @Validated AddLinkDto addLinkDto){
        return linkService.addLink(addLinkDto);
    }

    @GetMapping("/{id}")
    public ResponseResult<LinkPageVo> getById(@PathVariable Long id){
        return linkService.getLinkById(id);
    }

    @PutMapping
    public ResponseResult<?> updateLink(@RequestBody @Validated UpdateLinkDto updateLinkDto){
        return linkService.updateLink(updateLinkDto);
    }

    @DeleteMapping("/{id}")
    public ResponseResult<?> deleteLink(@PathVariable Long id){
        return ResponseResult.okResult(linkService.removeById(id));
    }
}
