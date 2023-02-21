package yoonleeverse.onlinejudge.config.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import yoonleeverse.onlinejudge.config.websocket.WebSocketMessage;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketMessageListener implements MessageListener {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final GenericJackson2JsonRedisSerializer jackson2JsonRedisSerializer;

    @Override
    public void onMessage(Message message, byte[] pattern) {

        WebSocketMessage msg = this.jackson2JsonRedisSerializer.deserialize(message.getBody(), WebSocketMessage.class);
        log.debug("[WebSocketMessageListener] onMessage msg[{}]", msg);


        String to = msg.getTo();
        String content = msg.getContent();

        switch (msg.getType()) {
            case RUN_RESULT:
                this.simpMessagingTemplate.convertAndSendToUser(to, "/queue/problem/run", content);
                break;
            case JUDGE_RESULT:
                this.simpMessagingTemplate.convertAndSendToUser(to, "/queue/notification", content);
                break;
        }
    }
}
