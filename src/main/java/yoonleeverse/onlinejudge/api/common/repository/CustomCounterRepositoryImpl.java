package yoonleeverse.onlinejudge.api.common.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import yoonleeverse.onlinejudge.api.common.entity.CounterEntity;

import java.util.Objects;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@RequiredArgsConstructor
public class CustomCounterRepositoryImpl implements CustomCounterRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public long getNextSequence(String collectionName) {
        Query query = query(where("_id").is(collectionName));
        Update update = new Update().inc("seq", 1);
        FindAndModifyOptions options = options().returnNew(true).upsert(true);

        CounterEntity counter = this.mongoTemplate.findAndModify(query, update, options, CounterEntity.class);
        return !Objects.isNull(counter) ? counter.getSeq() : 1;
    }
}
