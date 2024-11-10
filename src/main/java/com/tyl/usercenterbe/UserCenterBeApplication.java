package com.tyl.usercenterbe;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.tyl.usercenterbe.mapper")
public class UserCenterBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserCenterBeApplication.class, args);
    }

}
