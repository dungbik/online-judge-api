package yoonleeverse.onlinejudge.api.user.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@RedisHash(value = "TokenStorage", timeToLive = 14 * 24 * 60 * 60)
public class TokenStorage implements Serializable {
    @Id private String id;
    @Indexed private String userId;
}
