package yoonleeverse.onlinejudge.api.problem.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import yoonleeverse.onlinejudge.api.problem.entity.Problem;

public interface ProblemRepository extends MongoRepository<Problem, Long>, CustomProblemRepository {

    boolean existsByTitle(String title);
}
