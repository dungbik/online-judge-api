package yoonleeverse.onlinejudge.api.user.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@RedisHash(value = "OAuthLink", timeToLive = 1 * 60)
public class OAuthLink implements Serializable {

    // Redis 에만 사용되는 필드
    @Id private String id;

    // Mongo, Redis 모두에서 사용되는 필드
    private String provider;
    private String userId;
    private String email;
    private String avatarUrl;

    public static OAuthLink of(OAuthLink oAuthLink) {
        return new OAuthLink(null, oAuthLink.getProvider(), oAuthLink.getUserId(), oAuthLink.getEmail(), oAuthLink.getAvatarUrl());
    }
}
