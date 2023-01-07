package com.subhadev.billshare.userservice.entity;

import lombok.*;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(timeToLive = 5*60*60)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OtpEntity {

    private String id;
    private String otp;
}
