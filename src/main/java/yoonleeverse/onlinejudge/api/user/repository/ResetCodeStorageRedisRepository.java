package yoonleeverse.onlinejudge.api.user.repository;

import org.springframework.data.repository.CrudRepository;
import yoonleeverse.onlinejudge.api.user.entity.ResetCodeStorage;

public interface ResetCodeStorageRedisRepository extends CrudRepository<ResetCodeStorage, String> {

}
