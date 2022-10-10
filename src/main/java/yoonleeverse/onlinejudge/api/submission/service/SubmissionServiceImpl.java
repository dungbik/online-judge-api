package yoonleeverse.onlinejudge.api.submission.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yoonleeverse.onlinejudge.api.problem.entity.ProgrammingLanguage;
import yoonleeverse.onlinejudge.api.submission.dto.SubmitProblemRequest;
import yoonleeverse.onlinejudge.api.submission.dto.SubmitProblemResponse;
import yoonleeverse.onlinejudge.api.submission.entity.Submission;
import yoonleeverse.onlinejudge.api.submission.mapper.SubmissionMapper;
import yoonleeverse.onlinejudge.api.submission.repository.SubmissionRepository;
import yoonleeverse.onlinejudge.security.UserPrincipal;

@Service
@RequiredArgsConstructor
public class SubmissionServiceImpl implements SubmissionService {
    private final SubmissionRepository submissionRepository;
    private final JudgeService judgeService;
    private final SubmissionMapper submissionMapper;

    @Override
    public SubmitProblemResponse submitProblem(UserPrincipal userPrincipal, SubmitProblemRequest req) {
        validateSubmitProblemReq(req);

        Submission submission = this.submissionMapper.toEntity(req);
        submission.setUserId(userPrincipal.getId());
        this.submissionRepository.save(submission);

        this.judgeService.judge(submission);

        return new SubmitProblemResponse();
    }

    private static void validateSubmitProblemReq(SubmitProblemRequest req) {
        ProgrammingLanguage language = req.getLanguage();
        if (language == null) {
            throw new RuntimeException("채점이 불가능한 프로그래밍 언어입니다.");
        }
    }
}
