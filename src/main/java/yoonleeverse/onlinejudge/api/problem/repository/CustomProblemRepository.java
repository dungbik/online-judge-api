package yoonleeverse.onlinejudge.api.problem.repository;

import com.mongodb.client.result.UpdateResult;
import org.springframework.data.domain.Page;
import yoonleeverse.onlinejudge.api.problem.dto.GetAllProblemRequest;
import yoonleeverse.onlinejudge.api.problem.entity.Problem;

public interface CustomProblemRepository {

    Page<Problem> getAllProblem(GetAllProblemRequest req);

    UpdateResult addSuccessCount(long problemId);
}
