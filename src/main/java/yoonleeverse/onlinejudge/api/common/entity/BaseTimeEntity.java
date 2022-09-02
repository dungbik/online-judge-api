package yoonleeverse.onlinejudge.api.common.entity;

import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

public class BaseTimeEntity {
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
