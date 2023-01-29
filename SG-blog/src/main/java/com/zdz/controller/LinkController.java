package com.zdz.controller;

import com.zdz.domain.ResponseResult;
import com.zdz.domain.vo.LinkVo;
import com.zdz.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/link")
public class LinkController {
    @Autowired
    private LinkService linkService;

    @GetMapping("/getAllLink")
    public ResponseResult<List<LinkVo>> getAllLink(){
        return linkService.getAllLink();
    }
}
