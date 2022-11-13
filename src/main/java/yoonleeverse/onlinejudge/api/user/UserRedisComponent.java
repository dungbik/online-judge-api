package yoonleeverse.onlinejudge.api.user;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.server.Cookie;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import yoonleeverse.onlinejudge.api.user.entity.TokenStorage;
import yoonleeverse.onlinejudge.api.user.repository.TokenStorageRedisRepository;
import yoonleeverse.onlinejudge.api.user.repository.UserRepository;
import yoonleeverse.onlinejudge.config.AppProperties;
import yoonleeverse.onlinejudge.config.websocket.UserPrincipal;
import yoonleeverse.onlinejudge.security.AuthTokenProvider;

import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import static yoonleeverse.onlinejudge.api.common.constant.Constants.Cookie.REFRESH_TOKEN;

@Component
@RequiredArgsConstructor
public class UserRedisComponent {

    private final RedisTemplate redisTemplate;

    public void removeUser(UserPrincipal userPrincipal) {
        this.redisTemplate.delete("WS_USER:" + userPrincipal.getUserKey());
    }

    public void addUser(String serverName, UserPrincipal userPrincipal) {
        this.redisTemplate.opsForValue().set("WS_USER:" + userPrincipal.getUserKey(), serverName, 1, TimeUnit.DAYS);
    }
}
