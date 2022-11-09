package yoonleeverse.onlinejudge.config.websocket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import yoonleeverse.onlinejudge.api.common.constant.Constants.WebSocketMessageType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WebSocketMessage {

    private WebSocketMessageType type;
    private String content;

    private String to;
}
