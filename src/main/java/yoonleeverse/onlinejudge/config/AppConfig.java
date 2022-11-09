package yoonleeverse.onlinejudge.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.net.InetAddress;

@Configuration
@RequiredArgsConstructor
public class AppConfig {

    private static String SERVER_NAME;

    private final Environment environment;

    @Bean
    ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        return mapper;
    }

    public String getServerName() {

        if (StringUtils.isEmpty(SERVER_NAME)) {
            try {
                String hostName = InetAddress.getLocalHost().getHostName();
                Integer port = this.environment.getProperty("server.port", Integer.class);
                if (port == null) {
                    System.out.println("hi");
                    port = 8080;
                }

                SERVER_NAME = hostName + "_" + port;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return SERVER_NAME;
    }
}
