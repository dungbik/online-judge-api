package yoonleeverse.onlinejudge.api.user.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import yoonleeverse.onlinejudge.api.user.entity.UserEntity;

import java.util.Optional;

public interface UserRepository extends MongoRepository<UserEntity, String>, CustomUserRepository {

    Optional<UserEntity> findByEmail(String email);
}
