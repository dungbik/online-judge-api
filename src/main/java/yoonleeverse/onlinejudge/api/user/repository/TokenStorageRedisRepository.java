package yoonleeverse.onlinejudge.api.user.repository;

import org.springframework.data.repository.CrudRepository;
import yoonleeverse.onlinejudge.api.user.entity.TokenStorage;

import java.util.Optional;

public interface TokenStorageRedisRepository extends CrudRepository<TokenStorage, String> {

    Optional<TokenStorage> findByRefreshToken(String refreshToken);
}
