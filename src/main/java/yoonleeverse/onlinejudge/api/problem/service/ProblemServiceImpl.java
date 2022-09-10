package yoonleeverse.onlinejudge.api.problem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yoonleeverse.onlinejudge.api.common.repository.CounterRepository;
import yoonleeverse.onlinejudge.api.common.service.StorageService;
import yoonleeverse.onlinejudge.api.problem.dto.AddProblemRequest;
import yoonleeverse.onlinejudge.api.problem.dto.AddProblemResponse;
import yoonleeverse.onlinejudge.api.problem.entity.ProblemEntity;
import yoonleeverse.onlinejudge.api.problem.entity.ProgrammingLanguage;
import yoonleeverse.onlinejudge.api.problem.entity.TestCase;
import yoonleeverse.onlinejudge.api.problem.repository.ProblemRepository;
import yoonleeverse.onlinejudge.api.problem.repository.TestCaseRedisRepository;
import yoonleeverse.onlinejudge.security.UserPrincipal;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProblemServiceImpl implements ProblemService {

    private final ProblemRepository problemRepository;
    private final StorageService storageService;
    private final CounterRepository counterRepository;
    private final TestCaseRedisRepository testCaseRedisRepository;

    @Override
    public AddProblemResponse addProblem(UserPrincipal userPrincipal, AddProblemRequest req) {
        AddProblemResponse addProblemResponse = new AddProblemResponse();

        try {
            boolean isExistTitle = problemRepository.existsByTitle(req.getTitle());
            if (isExistTitle) {
                throw new RuntimeException("이미 존재하는 제목입니다.");
            }

            long problemId = counterRepository.getNextSequence("problems");
            List<TestCase> testCases = storageService.loadTestCase(req.getFile(), "problem/" + problemId);
            ProblemEntity problem = ProblemEntity.builder()
                    .id(problemId)
                    .title(req.getTitle())
                    .timeLimit(req.getTimeLimit())
                    .memoryLimit(req.getMemoryLimit())
                    .desc(req.getDesc())
                    .inputDesc(req.getInputDesc())
                    .outputDesc(req.getOutputDesc())
                    .testCaseExamples(req.getTestCaseExamples())
                    .languages(req.getLanguages().stream()
                            .map(e -> ProgrammingLanguage.valueOf(e.toUpperCase())).collect(Collectors.toList()))
                    .testCases(testCases)
                    .userId(userPrincipal.getId())
                    .build();
            problemRepository.insert(problem);

            testCaseRedisRepository.save(problemId, testCases);
        } catch (Exception e) {
            addProblemResponse.setSuccess(false);
            addProblemResponse.setErrMsg(e.getMessage());
        }

        return addProblemResponse;
    }
}
