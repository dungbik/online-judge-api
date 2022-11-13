package yoonleeverse.onlinejudge.util;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {

    private final static ObjectMapper mapper = new ObjectMapper();

    public static String makeJson(Object obj) {
        String json = "";
        try {
            json = mapper.writeValueAsString(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return json;
    }
}
