package yoonleeverse.onlinejudge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.redis.core.RedisKeyValueAdapter;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@SpringBootApplication(exclude = {RedisAutoConfiguration.class})
@EnableMongoAuditing(modifyOnCreate = false)
@ConfigurationPropertiesScan
public class OnlineJudgeApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlineJudgeApiApplication.class, args);
    }

}
