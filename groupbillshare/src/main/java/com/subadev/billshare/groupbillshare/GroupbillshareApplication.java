package com.subadev.billshare.groupbillshare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;


@SpringBootApplication
public class GroupbillshareApplication {

    public static void main(String[] args) {
        SpringApplication.run(GroupbillshareApplication.class, args);
    }

}
