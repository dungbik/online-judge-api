package yoonleeverse.onlinejudge.api.common.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import yoonleeverse.onlinejudge.api.common.entity.CounterEntity;

public interface CounterRepository extends MongoRepository<CounterEntity, String>, CustomCounterRepository {

}
