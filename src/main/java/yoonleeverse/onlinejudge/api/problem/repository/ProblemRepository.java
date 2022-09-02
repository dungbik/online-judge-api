package yoonleeverse.onlinejudge.api.problem.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import yoonleeverse.onlinejudge.api.problem.entity.ProblemEntity;

public interface ProblemRepository extends MongoRepository<ProblemEntity, Long>, CustomProblemRepository {

    boolean existsByTitle(String title);
}
