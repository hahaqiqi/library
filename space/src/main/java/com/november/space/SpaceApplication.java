package com.november.space;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("com.november.space.dao")
@ComponentScan("com.november")
public class SpaceApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpaceApplication.class, args);
    }
}
