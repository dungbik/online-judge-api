package yoonleeverse.onlinejudge.api.problem.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import yoonleeverse.onlinejudge.api.problem.entity.Tag;

import java.util.Optional;

public interface TagRepository extends MongoRepository<Tag, Long> {
    Optional<Tag> findByName(String name);
}
