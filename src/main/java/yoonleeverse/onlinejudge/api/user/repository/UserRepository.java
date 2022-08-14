package yoonleeverse.onlinejudge.api.user.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import yoonleeverse.onlinejudge.api.user.entity.OAuthLink;
import yoonleeverse.onlinejudge.api.user.entity.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<UserEntity, String>, CustomUserRepository {

    @Query(value = "{ 'links' : { '$elemMatch' : { 'provider' : ?0, 'userId': ?1 } } }")
    Optional<UserEntity> findByProviderAndUserId(String provider, String userId);

    boolean existsByIdOrName(String id, String name);
}
