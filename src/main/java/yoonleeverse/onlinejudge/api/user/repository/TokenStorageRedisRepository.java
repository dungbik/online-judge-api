package yoonleeverse.onlinejudge.api.user.repository;

import org.springframework.data.repository.CrudRepository;
import yoonleeverse.onlinejudge.api.user.entity.TokenStorage;

public interface TokenStorageRedisRepository extends CrudRepository<TokenStorage, String> {

    void deleteByUserId(String userId);

}
