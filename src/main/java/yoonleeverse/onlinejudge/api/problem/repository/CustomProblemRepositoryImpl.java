package yoonleeverse.onlinejudge.api.problem.repository;

import com.mongodb.client.result.UpdateResult;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.CollectionUtils;
import yoonleeverse.onlinejudge.api.problem.dto.GetAllProblemRequest;
import yoonleeverse.onlinejudge.api.problem.entity.Problem;
import yoonleeverse.onlinejudge.api.problem.entity.ProgrammingLanguage;
import yoonleeverse.onlinejudge.api.problem.entity.Tag;
import yoonleeverse.onlinejudge.util.NumberUtil;

import java.util.List;
import java.util.stream.Collectors;

import static yoonleeverse.onlinejudge.api.common.constant.Constants.PROBLEM_MAX_SIZE;

@RequiredArgsConstructor
public class CustomProblemRepositoryImpl implements CustomProblemRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public Page<Problem> getAllProblem(GetAllProblemRequest req) {
        String title = req.getTitle();
        List<ProgrammingLanguage> languages = req.getLanguages();
        List<Long> tags = req.getTags();
        int page = NumberUtil.toPage(req.getPage());

        Pageable pageable = PageRequest.of(page, PROBLEM_MAX_SIZE, Sort.by(Sort.Direction.DESC, "id"));
        Criteria criteria = new Criteria();
        if (StringUtils.isNotEmpty(title)) {
            criteria.and("title").regex(title, "i");
        }
        if (!CollectionUtils.isEmpty(languages)) {
            criteria.and("languages").in(languages);
        }
        if (!CollectionUtils.isEmpty(tags)) {
            criteria.and("tags").in(tags.stream().map((id) -> Tag.builder().id(id).build()).collect(Collectors.toList()));
        }

        Query query = Query.query(criteria).with(pageable);

        List<Problem> problems = this.mongoTemplate.find(query, Problem.class);
        Page<Problem> problemPage = PageableExecutionUtils.getPage(
                problems,
                pageable,
                () -> mongoTemplate.count(query.skip(-1).limit(-1), Problem.class)
        );

        return problemPage;
    }

    @Override
    public UpdateResult addSuccessCount(long problemId) {
        Criteria criteria = Criteria.where("id").is(problemId);
        Query query = Query.query(criteria);

        Update update = new Update();
        update.inc("submissionHistory.successCount", 1);

        return this.mongoTemplate.updateFirst(query, update, Problem.class);
    }
}
