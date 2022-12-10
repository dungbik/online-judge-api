package yoonleeverse.onlinejudge.api.submission.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import yoonleeverse.onlinejudge.api.problem.entity.Problem;
import yoonleeverse.onlinejudge.api.problem.entity.ProgrammingLanguage;
import yoonleeverse.onlinejudge.api.problem.repository.ProblemRepository;
import yoonleeverse.onlinejudge.api.submission.dto.GetAllSubmissionRequest;
import yoonleeverse.onlinejudge.api.submission.dto.GetAllSubmissionResponse;
import yoonleeverse.onlinejudge.api.submission.dto.SubmitProblemRequest;
import yoonleeverse.onlinejudge.api.submission.dto.SubmitProblemResponse;
import yoonleeverse.onlinejudge.api.submission.entity.Submission;
import yoonleeverse.onlinejudge.api.submission.mapper.SubmissionMapper;
import yoonleeverse.onlinejudge.api.submission.repository.SubmissionRepository;
import yoonleeverse.onlinejudge.security.UserPrincipal;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubmissionServiceImpl implements SubmissionService {
    private final SubmissionRepository submissionRepository;
    private final JudgeService judgeService;
    private final SubmissionMapper submissionMapper;
    private final ProblemRepository problemRepository;

    @Override
    public SubmitProblemResponse submitProblem(UserPrincipal userPrincipal, SubmitProblemRequest req) {
        validateSubmitProblemReq(req);

        Submission submission = this.submissionMapper.toEntity(req);
        submission.setUserId(userPrincipal.getEmail());
        this.submissionRepository.save(submission);

        this.judgeService.judge(submission);

        return new SubmitProblemResponse();
    }

    @Override
    public GetAllSubmissionResponse getAllSubmission(GetAllSubmissionRequest req) {
        Page<Submission> submissionPage = this.submissionRepository.getAllSubmission(req);

        GetAllSubmissionResponse response = new GetAllSubmissionResponse();
        response.setPage(this.submissionMapper.toPageDto(submissionPage));

        if (!submissionPage.isEmpty()) {
            List<Submission> problems = submissionPage.getContent();
            response.setSubmissions(this.submissionMapper.toDtoList(problems));
        }

        return response;
    }


    private void validateSubmitProblemReq(SubmitProblemRequest req) {
        long problemId = req.getProblemId();
        ProgrammingLanguage language = req.getLanguage();
        if (language == null) {
            throw new RuntimeException("채점이 불가능한 프로그래밍 언어입니다.");
        }

        Problem problem = this.problemRepository.findById(problemId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 문제입니다."));

        boolean allowLanguage = problem.getLanguages().contains(language);
        if (!allowLanguage) {
            throw new RuntimeException("채점이 가능한 프로그래밍 언어가 아닙니다.");
        }

    }
}
