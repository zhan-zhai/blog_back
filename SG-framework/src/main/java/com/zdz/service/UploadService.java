package com.zdz.service;

import com.zdz.domain.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

public interface UploadService {
    ResponseResult<String> upload(MultipartFile file);
}
