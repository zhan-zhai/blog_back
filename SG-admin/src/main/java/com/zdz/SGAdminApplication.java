package com.zdz;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("com.zdz.mapper")
public class SGAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(SGAdminApplication.class,args);
    }
}
