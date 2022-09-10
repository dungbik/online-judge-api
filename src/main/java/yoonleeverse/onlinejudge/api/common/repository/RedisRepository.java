package yoonleeverse.onlinejudge.api.common.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RedisRepository {

    protected <T> T readValue(String json, TypeReference<T> valueTypeRef) {
        ObjectMapper mapper = new ObjectMapper();
        T obj = null;
        try {
            obj = mapper.readValue(json, valueTypeRef);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return obj;
    }

    protected String writeValue(Object obj) {
        ObjectMapper mapper = new ObjectMapper();
        String json = null;
        try {
            json = mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }


}
