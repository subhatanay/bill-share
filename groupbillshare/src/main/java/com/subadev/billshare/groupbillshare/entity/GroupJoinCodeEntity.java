package com.subadev.billshare.groupbillshare.entity;

import lombok.*;
import org.springframework.data.redis.core.RedisHash;

@RedisHash
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupJoinCodeEntity {
    private String id;
    private String groupId;
    private String joinCode;
}
