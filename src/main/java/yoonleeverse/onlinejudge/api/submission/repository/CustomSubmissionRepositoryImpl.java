package yoonleeverse.onlinejudge.api.submission.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import yoonleeverse.onlinejudge.api.problem.entity.ProgrammingLanguage;
import yoonleeverse.onlinejudge.api.submission.dto.GetAllSubmissionRequest;
import yoonleeverse.onlinejudge.api.submission.entity.Submission;
import yoonleeverse.onlinejudge.util.NumberUtil;

import java.util.List;

import static yoonleeverse.onlinejudge.api.common.constant.Constants.SUBMISSION_MAX_SIZE;

@RequiredArgsConstructor
public class CustomSubmissionRepositoryImpl implements CustomSubmissionRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public Page<Submission> getAllSubmission(GetAllSubmissionRequest req) {
        Long problemId = req.getProblemId();
        ProgrammingLanguage language = req.getLanguage();
        String userId = req.getUserId();
        boolean isRanking = req.isRanking();
        int page = NumberUtil.toPage(req.getPage());

        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        if (isRanking && problemId != null) {
            sort = Sort.by(Sort.Direction.ASC, "memory", "realTime", "codeLength");
        }

        Pageable pageable = PageRequest.of(page, SUBMISSION_MAX_SIZE, sort);
        Criteria criteria = Criteria.where("isJudge").is(true);

        if (problemId != null) {
            criteria.and("problemId").is(problemId);
        }
        if (language != null) {
            criteria.and("language").is(language);
        }
        if (userId != null) {
            criteria.and("userId").is(userId);
        }

        Query query = Query.query(criteria).with(pageable);
        List<Submission> submissions = this.mongoTemplate.find(query, Submission.class);
        Page<Submission> submissionPage = PageableExecutionUtils.getPage(
                submissions,
                pageable,
                () -> mongoTemplate.count(query.skip(-1).limit(-1), Submission.class)
        );

        return submissionPage;
    }
}
