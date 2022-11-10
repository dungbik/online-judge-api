package yoonleeverse.onlinejudge.config.websocket;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yoonleeverse.onlinejudge.api.common.constant.Constants.WebSocketMessageType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WebSocketMessage {

    private WebSocketMessageType type;
    private String content;

    private String to;

    @Data
    @NoArgsConstructor
    public static class Notification {
        private Variant variant;
        private String message;
    }

    @Getter
    public enum Variant {
        NORMAL("normal"),
        ERROR("error"),
        WARNING("warning"),
        SUCCESS("success")
        ;

        @JsonValue
        private String value;

        Variant(String value) {
            this.value = value;
        }
    }
}
