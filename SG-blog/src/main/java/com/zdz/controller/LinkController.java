package com.zdz.controller;

import com.zdz.domain.ResponseResult;
import com.zdz.domain.vo.LinkVo;
import com.zdz.service.LinkService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/link")
@Api(tags = "友链相关接口")
public class LinkController {
    @Autowired
    private LinkService linkService;

    @GetMapping("/getAllLink")
    @ApiOperation("获取友链列表")
    public ResponseResult<List<LinkVo>> getAllLink(){
        return linkService.getAllLink();
    }
}
