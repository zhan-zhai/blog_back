package com.zdz;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.zdz.mapper")
public class SGBlogApplication {
    public static void main(String[] args) {
        SpringApplication.run(SGBlogApplication.class,args);
    }
}
