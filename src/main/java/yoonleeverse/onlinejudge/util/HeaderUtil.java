package yoonleeverse.onlinejudge.util;

import org.springframework.messaging.simp.stomp.StompHeaderAccessor;

import javax.servlet.http.HttpServletRequest;

public class HeaderUtil {

    private final static String HEADER_AUTHORIZATION = "Authorization";
    private final static String TOKEN_PREFIX = "Bearer ";

    public static String getAccessToken(HttpServletRequest request) {
        String headerValue = request.getHeader(HEADER_AUTHORIZATION);

        return getAccessToken(headerValue);
    }

    public static String getAccessToken(StompHeaderAccessor accessor) {
        String headerValue = accessor.getFirstNativeHeader(HEADER_AUTHORIZATION);

        return getAccessToken(headerValue);
    }

    private static String getAccessToken(String headerValue) {
        if (headerValue == null) {
            return null;
        }

        if (headerValue.startsWith(TOKEN_PREFIX)) {
            return headerValue.substring(TOKEN_PREFIX.length());
        }

        return null;
    }
}