package yoonleeverse.onlinejudge.api.problem.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import yoonleeverse.onlinejudge.api.common.repository.CounterRepository;
import yoonleeverse.onlinejudge.api.problem.entity.ProblemEntity;

@RequiredArgsConstructor
public class CustomProblemRepositoryImpl implements CustomProblemRepository {

    private final MongoTemplate mongoTemplate;
    private final CounterRepository counterRepository;

    @Override
    public ProblemEntity saveWithId(ProblemEntity problem) {
        if (problem.getId() == null) {
            problem.setId(counterRepository.getNextSequence("problems"));
        }

        return this.mongoTemplate.save(problem);
    }
}
