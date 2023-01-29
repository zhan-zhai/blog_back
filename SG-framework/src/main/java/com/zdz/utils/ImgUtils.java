package com.zdz.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class ImgUtils {
    public static String generateFilePath(String originalFilename) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd/");
        String datePath = format.format(new Date());
//        生成UUID
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
//        获取到文件后缀名，截取字符串
        int index = originalFilename.lastIndexOf(".");
        String fileType = originalFilename.substring(index);
        return datePath + uuid + fileType;
    }
}