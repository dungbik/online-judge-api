package yoonleeverse.onlinejudge.api.submission.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yoonleeverse.onlinejudge.api.problem.entity.ProgrammingLanguage;
import yoonleeverse.onlinejudge.api.submission.dto.SubmitProblemRequest;
import yoonleeverse.onlinejudge.api.submission.dto.SubmitProblemResponse;
import yoonleeverse.onlinejudge.api.submission.entity.JudgeStatus;
import yoonleeverse.onlinejudge.api.submission.entity.Submission;
import yoonleeverse.onlinejudge.api.submission.repository.SubmissionRepository;
import yoonleeverse.onlinejudge.security.UserPrincipal;

@Service
@RequiredArgsConstructor
public class SubmissionServiceImpl implements SubmissionService {
    private final SubmissionRepository submissionRepository;
    private final JudgeService judgeService;

    @Override
    public SubmitProblemResponse submitProblem(UserPrincipal userPrincipal, SubmitProblemRequest req) {
        SubmitProblemResponse response = new SubmitProblemResponse();
        Submission submission = null;
        try {
            ProgrammingLanguage language = ProgrammingLanguage.valueOf(req.getLanguage());
            if (language == null) {
                throw new RuntimeException("채점이 불가능한 프로그래밍 언어입니다.");
            }

            submission = Submission.builder()
                    .problemId(req.getProblemId())
                    .language(language.name())
                    .code(req.getCode())
                    .userId(userPrincipal.getId())
                    .status(JudgeStatus.PENDING)
                    .build();
            submissionRepository.save(submission);

            judgeService.judge(submission);
        } catch (Exception e) {
            if (submission != null) {
                submission.setStatus(JudgeStatus.FAIL);
                submissionRepository.save(submission);
            }

            response.setSuccess(false);
            response.setErrMsg(e.getMessage());
        }

        return response;
    }
}
