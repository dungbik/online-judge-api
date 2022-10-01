package yoonleeverse.onlinejudge.api.problem.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import yoonleeverse.onlinejudge.api.problem.dto.GetAllProblemRequest;
import yoonleeverse.onlinejudge.api.problem.entity.Problem;
import yoonleeverse.onlinejudge.util.NumberUtil;

import java.util.List;

import static yoonleeverse.onlinejudge.api.common.constant.Constants.PROBLEM_MAX_SIZE;

@RequiredArgsConstructor
public class CustomProblemRepositoryImpl implements CustomProblemRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public Page<Problem> getAllProblem(GetAllProblemRequest req) {
        String title = req.getTitle();
        int page = NumberUtil.toPage(req.getPage());

        Pageable pageable = PageRequest.of(page, PROBLEM_MAX_SIZE, Sort.by(Sort.Direction.DESC, "id"));

        Criteria criteria = new Criteria();
        if (title != null) {
            criteria.andOperator(Criteria.where("title").regex(title, "i"));
        }

        Query query = Query.query(criteria);
        query.with(pageable);

        List<Problem> problems = this.mongoTemplate.find(query, Problem.class);
        Page<Problem> problemPage = PageableExecutionUtils.getPage(
                problems,
                pageable,
                () -> mongoTemplate.count(query.skip(-1).limit(-1), Problem.class)
        );

        return problemPage;
    }
}
