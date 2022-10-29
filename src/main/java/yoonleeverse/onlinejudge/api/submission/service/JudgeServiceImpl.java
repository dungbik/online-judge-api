package yoonleeverse.onlinejudge.api.submission.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import yoonleeverse.onlinejudge.api.problem.entity.Problem;
import yoonleeverse.onlinejudge.api.problem.entity.TestCase;
import yoonleeverse.onlinejudge.api.problem.repository.ProblemRepository;
import yoonleeverse.onlinejudge.api.problem.repository.TestCaseRedisRepository;
import yoonleeverse.onlinejudge.api.submission.dto.CompleteMessage;
import yoonleeverse.onlinejudge.api.submission.dto.JudgeMessage;
import yoonleeverse.onlinejudge.api.submission.dto.RunResult;
import yoonleeverse.onlinejudge.api.submission.dto.TestCaseInput;
import yoonleeverse.onlinejudge.api.submission.entity.JudgeStatus;
import yoonleeverse.onlinejudge.api.submission.entity.Submission;
import yoonleeverse.onlinejudge.api.submission.repository.SubmissionRepository;

import java.util.*;
import java.util.stream.Collectors;

import static yoonleeverse.onlinejudge.config.RabbitMQConfig.EXCHANGE_NAME;
import static yoonleeverse.onlinejudge.config.RabbitMQConfig.JUDGE_ROUTING_KEY;

@Service
@Slf4j
@RequiredArgsConstructor
public class JudgeServiceImpl implements JudgeService {

    private final RabbitTemplate rabbitTemplate;
    private final TestCaseRedisRepository testCaseRedisRepository;
    private final ProblemRepository problemRepository;
    private final SubmissionRepository submissionRepository;

    @Override
    public void judge(Submission submission) {
        long problemId = submission.getProblemId();
        Problem problem = this.problemRepository.findById(problemId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 문제입니다."));

        JudgeMessage judgeMessage = new JudgeMessage();
        judgeMessage.setSubmissionId(submission.getId());
        judgeMessage.setProblemId(problemId);
        judgeMessage.setMaxCpuTime(problem.getTimeLimit());
        judgeMessage.setMaxRealTime(problem.getTimeLimit());
        judgeMessage.setMaxMemory(problem.getMemoryLimit());
        judgeMessage.setCode(submission.getCode());
        judgeMessage.setLanguage(submission.getLanguage());

        List<TestCase> testCases = this.testCaseRedisRepository.find(problemId);
        if (testCases != null) {
            List<TestCaseInput> list = testCases.stream()
                    .map(e -> new TestCaseInput(e.getId(), e.getInput()))
                    .collect(Collectors.toList());
            judgeMessage.setInputs(list);

            log.debug("judge {}", judgeMessage);
            this.rabbitTemplate.convertAndSend(EXCHANGE_NAME, JUDGE_ROUTING_KEY, judgeMessage);
            problem.getSubmissionHistory().addTotalCount();
            this.problemRepository.save(problem);
        }
    }

    @Override
    public void completeJudge(CompleteMessage completeMessage) {
        boolean ok = false;
        Submission submission = null;

        try {
            submission = this.submissionRepository.findById(completeMessage.getSubmissionId())
                    .orElseThrow(() -> new RuntimeException("존재하지 않는 제출내역입니다."));

            List<TestCase> testCases = this.testCaseRedisRepository.find(completeMessage.getProblemId());
            if (testCases != null) {
                for (RunResult runResult : completeMessage.getResults()) {
                    if (runResult.getResult() != JudgeStatus.SUCCESS.getValue()) {
                        submission.setStatus(JudgeStatus.valueOf(runResult.getResult()), null, null);
                        break;
                    }
                }

                if (submission.getStatus() == JudgeStatus.PENDING) {
                    Map<Integer, String> testCaseMap = testCases.stream()
                            .collect(Collectors.toMap(e -> e.getId(), e -> e.getOutputMD5()));
                    ok = (completeMessage.getResults().size() - 1) == testCases.size() &&
                            completeMessage.getResults().stream()
                                    .filter(runResult -> runResult.getId() > 0)
                                    .allMatch(runResult -> testCaseMap.get(runResult.getId()).equalsIgnoreCase(runResult.getOutputMD5()));

                    if (ok) {
                        long maxMemory = 0;
                        int maxRealTime = 0;
                        for (RunResult runResult : completeMessage.getResults()) {
                            maxMemory = Math.max(maxMemory, runResult.getMemory());
                            maxRealTime =  Math.max(maxRealTime, runResult.getReal_time());
                        }
                        submission.setStatus(JudgeStatus.SUCCESS, maxMemory, maxRealTime);
                        this.problemRepository.addSuccessCount(submission.getProblemId());
                    } else {
                        submission.setStatus(JudgeStatus.WRONG_ANSWER, null, null);
                    }
                }
            }
        } catch (Exception e) {
            submission.setStatus(JudgeStatus.UNK_ERROR, null, null);
            log.debug("{}", e.getMessage());
        } finally {
            if (submission != null) {
                this.submissionRepository.save(submission);
            }
            log.debug("completeJudge {} {}", ok, completeMessage);
        }
    }
}
