package com.bootcamp.onlineschool;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class OnlineSchoolApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlineSchoolApplication.class, args);
    }
}