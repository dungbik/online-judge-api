package yoonleeverse.onlinejudge.api.common.entity;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Document("counters")
public class CounterEntity {
    @Id private String collectionName;
    private long seq;
}
