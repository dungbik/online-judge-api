package yoonleeverse.onlinejudge.config.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import yoonleeverse.onlinejudge.config.websocket.WebSocketMessage;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisPublisher {

    private final RedisTemplate redisTemplate;

    public void publishMessage(WebSocketMessage message) {

        String to = message.getTo();

        String serverName = (String) this.redisTemplate.opsForValue().get("WS_USER:" + to);
        log.debug("[RedisPublisher] publishMessage message[{}] serverName[{}]", message, serverName);

        if (serverName != null) {
            this.redisTemplate.convertAndSend("WS_USER:" + serverName, message);
        }


    }

}
