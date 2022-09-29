package yoonleeverse.onlinejudge.api.problem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yoonleeverse.onlinejudge.api.common.constant.MongoDB;
import yoonleeverse.onlinejudge.api.common.repository.CounterRepository;
import yoonleeverse.onlinejudge.api.common.service.StorageService;
import yoonleeverse.onlinejudge.api.problem.dto.AddProblemRequest;
import yoonleeverse.onlinejudge.api.problem.dto.AddProblemResponse;
import yoonleeverse.onlinejudge.api.problem.entity.Problem;
import yoonleeverse.onlinejudge.api.problem.entity.Tag;
import yoonleeverse.onlinejudge.api.problem.entity.TestCase;
import yoonleeverse.onlinejudge.api.problem.repository.ProblemRepository;
import yoonleeverse.onlinejudge.api.problem.repository.TagRepository;
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
    private final TagRepository tagRepository;

    @Override
    public AddProblemResponse addProblem(UserPrincipal userPrincipal, AddProblemRequest req) {
        AddProblemResponse addProblemResponse = new AddProblemResponse();

        try {
            boolean isExistTitle = problemRepository.existsByTitle(req.getTitle());
            if (isExistTitle) {
                throw new RuntimeException("이미 존재하는 제목입니다.");
            }

            List<Tag> tags = req.getTags().stream()
                    .map(this::getTag)
                    .collect(Collectors.toList());

            long problemId = counterRepository.getNextSequence(MongoDB.PROBLEM);
            List<TestCase> testCases = storageService.loadTestCase(req.getFile(), "problem/" + problemId);

            Problem problem = Problem.makeProblem(problemId, req, testCases, tags, userPrincipal.getId());
            problemRepository.insert(problem);
            tagRepository.saveAll(tags);

            testCaseRedisRepository.save(problemId, testCases);
        } catch (Exception e) {
            addProblemResponse.setSuccess(false);
            addProblemResponse.setErrMsg(e.getMessage());
        }

        return addProblemResponse;
    }

    private Tag getTag(String name) {
       return tagRepository.findByName(name)
               .orElse(Tag.makeTag(counterRepository.getNextSequence(MongoDB.TAG), name));
    }
}
