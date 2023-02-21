package yoonleeverse.onlinejudge.api.submission.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import yoonleeverse.onlinejudge.api.common.constant.Constants;
import yoonleeverse.onlinejudge.api.problem.entity.Problem;
import yoonleeverse.onlinejudge.api.problem.entity.TestCase;
import yoonleeverse.onlinejudge.api.problem.repository.ProblemRepository;
import yoonleeverse.onlinejudge.api.problem.repository.TestCaseRedisRepository;
import yoonleeverse.onlinejudge.api.submission.dto.CompleteMessage;
import yoonleeverse.onlinejudge.api.submission.dto.JudgeMessage;
import yoonleeverse.onlinejudge.api.submission.dto.RunResult;
import yoonleeverse.onlinejudge.api.submission.dto.TestCaseInput;
import yoonleeverse.onlinejudge.api.submission.entity.JudgeResult;
import yoonleeverse.onlinejudge.api.submission.entity.JudgeStatus;
import yoonleeverse.onlinejudge.api.submission.entity.Submission;
import yoonleeverse.onlinejudge.api.submission.mapper.SubmissionMapper;
import yoonleeverse.onlinejudge.api.submission.repository.SubmissionRepository;
import yoonleeverse.onlinejudge.config.redis.RedisPublisher;
import yoonleeverse.onlinejudge.config.websocket.WebSocketMessage;
import yoonleeverse.onlinejudge.config.websocket.WebSocketMessage.Notification;
import yoonleeverse.onlinejudge.util.JsonUtil;
import yoonleeverse.onlinejudge.util.StringUtil;

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
    private final SubmissionMapper submissionMapper;
    private final RedisPublisher redisPublisher;
    private final ObjectMapper objectMapper;

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

        List<TestCase> testCases = null;
        if (submission.isJudge()) {
            testCases = this.testCaseRedisRepository.find(problemId);
        } else {
            testCases = problem.getTestCaseExamples();
        }
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
        Problem problem = null;

        try {
            problem = this.problemRepository.findById(completeMessage.getProblemId())
                    .orElseThrow(() -> new RuntimeException("존재하지 않는 문제입니다."));
            submission = this.submissionRepository.findById(completeMessage.getSubmissionId())
                    .orElseThrow(() -> new RuntimeException("존재하지 않는 제출내역입니다."));

            List<TestCase> testCases = null;

            if (submission.isJudge()) {
                testCases = this.testCaseRedisRepository.find(completeMessage.getProblemId());
            } else {
                testCases = problem.getTestCaseExamples();
            }

            if (testCases != null) {
                List<RunResult> results = completeMessage.getResults();
                for (RunResult runResult : results) {
                    if (runResult.getResult() != JudgeStatus.SUCCESS.getValue()) {
                        submission.setStatus(JudgeStatus.valueOf(runResult.getResult()));
                        break;
                    }
                }

                if (submission.getStatus() == JudgeStatus.PENDING) {
                    List<JudgeResult> resultList = this.submissionMapper.toResultList(results);

                    Map<Integer, String> testCaseMap = testCases.stream()
                            .collect(Collectors.toMap(e -> e.getId(), e -> e.getOutputMD5()));
                    log.debug("[completeJudge] testCaseMap[{}] resultList[{}] testCases[{}]", testCaseMap, resultList, testCases);
                    if ((resultList.size() - 1) == testCases.size()) {
                        ok = true;
                        resultList.get(0).setCorrect(true);
                        for (JudgeResult result : resultList.subList(1, testCases.size())) {
                            String answerMD5 = testCaseMap.get(result.getId());
                            log.debug("[completeJudge] id[{}] answerMD5[{}] submittedMD5[{}]", result.getId(), answerMD5, result.getOutputMD5());
                            if (answerMD5.equalsIgnoreCase(result.getOutputMD5())) {
                                result.setCorrect(true);
                            } else {
                                result.setCorrect(false);
                                ok = false;
                            }
                        }
                    }

                    long maxMemory = 0;
                    int maxRealTime = 0;
                    for (RunResult runResult : results) {
                        maxMemory = Math.max(maxMemory, runResult.getMemory());
                        maxRealTime = Math.max(maxRealTime, runResult.getReal_time());
                    }
                    if (ok) {
                        submission.setStatus(JudgeStatus.SUCCESS, maxMemory, maxRealTime, resultList);
                        if (submission.isJudge()) {
                            this.problemRepository.addSuccessCount(submission.getProblemId());
                        }
                    } else {
                        submission.setStatus(JudgeStatus.WRONG_ANSWER, maxMemory, maxRealTime, resultList);
                    }
                }
            }

        } catch (Exception e) {
            submission.setStatus(JudgeStatus.UNK_ERROR);
            log.debug("{}", e.getMessage());
        } finally {
            if (submission != null) {
                this.submissionRepository.save(submission);
            }
            log.debug("completeJudge {} {}", ok, completeMessage);

            String to = StringUtil.encryptMD5(submission.getUserId());
            if (submission.isJudge()) {
                Notification notification = new Notification();
                notification.setVariant(WebSocketMessage.Variant.SUCCESS);
                notification.setMessage("제출한 문제가 채점이 완료되었습니다.");

                redisPublisher.publishMessage(new WebSocketMessage(Constants.WebSocketMessageType.JUDGE_RESULT, JsonUtil.makeJson(notification), to));
            } else {
                String content = "";
                try {
                    content = this.objectMapper.writeValueAsString(submission);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                redisPublisher.publishMessage(new WebSocketMessage(Constants.WebSocketMessageType.RUN_RESULT, content, to));
            }
        }
    }
}
