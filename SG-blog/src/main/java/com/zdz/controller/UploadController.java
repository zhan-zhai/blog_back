package com.zdz.controller;

import com.zdz.domain.ResponseResult;
import com.zdz.service.UploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@RestController
@Validated
@Api("图片上传相关接口")
public class UploadController {
    @Autowired
    private UploadService uploadService;

    @PostMapping("/upload")
    @ApiOperation("上传头像")
    public ResponseResult<String> upload(@RequestParam("img") @NotNull(message = "上传文件不可为空") MultipartFile img){
        return uploadService.upload(img);
    }
}
