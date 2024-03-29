package yoonleeverse.onlinejudge.config.websocket;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;
import yoonleeverse.onlinejudge.api.user.UserComponent;
import yoonleeverse.onlinejudge.api.user.UserRedisComponent;
import yoonleeverse.onlinejudge.config.AppConfig;
import yoonleeverse.onlinejudge.security.AuthTokenProvider;
import yoonleeverse.onlinejudge.util.HeaderUtil;

import java.util.concurrent.TimeUnit;


@RequiredArgsConstructor
@Component
@Slf4j
public class StompHandler implements ChannelInterceptor {
    private final AuthTokenProvider authTokenProvider;
    private final AppConfig appConfig;
    private final UserRedisComponent userRedisComponent;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor = StompHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        String sessionId = accessor.getSessionId();

        StompCommand command = accessor.getCommand();
        if (command == null) {
            return message;
        }

        String serverName = this.appConfig.getServerName();
        switch (command) {
            case CONNECT:
                String accessToken = HeaderUtil.getAccessToken(accessor);
                if (StringUtils.isNotEmpty(accessToken)) {
                    Claims claims = authTokenProvider.getTokenClaims(accessToken);
                    if (claims != null) {
                        String id = claims.getSubject();
                        UserPrincipal userPrincipal = new UserPrincipal(id);
                        accessor.setUser(userPrincipal);
                        this.userRedisComponent.addUser(serverName, userPrincipal);
                    }
                }
                log.debug("[STOMP] CONNECT - sessionId={} accessToken={} user={}", sessionId, accessToken, accessor.getUser());
                break;
            case DISCONNECT:
                UserPrincipal userPrincipal = (UserPrincipal) accessor.getUser();
                if (userPrincipal != null) {
                    this.userRedisComponent.removeUser(userPrincipal);
                }
                log.debug("[STOMP] DISCONNECT - sessionId={} user={}", sessionId, accessor.getUser());
                break;
        }

        return message;
    }
}
