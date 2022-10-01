package yoonleeverse.onlinejudge.api.problem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import yoonleeverse.onlinejudge.api.common.constant.MongoDB;
import yoonleeverse.onlinejudge.api.common.repository.CounterRepository;
import yoonleeverse.onlinejudge.api.common.service.StorageService;
import yoonleeverse.onlinejudge.api.problem.dto.*;
import yoonleeverse.onlinejudge.api.problem.entity.*;
import yoonleeverse.onlinejudge.api.problem.mapper.ProblemMapper;
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
    private final ProblemMapper problemMapper;

    @Override
    public AddProblemResponse addProblem(UserPrincipal userPrincipal, AddProblemRequest req, MultipartFile file) {
        boolean isExistTitle = problemRepository.existsByTitle(req.getTitle());
        if (isExistTitle) {
            throw new RuntimeException("이미 존재하는 제목입니다.");
        }

        List<Tag> tags = req.getTags().stream()
                .map(this::findOrMakeTag)
                .collect(Collectors.toList());

        long problemId = counterRepository.getNextSequence(MongoDB.PROBLEM);
        List<TestCase> testCases = storageService.loadTestCase(file, "problem/" + problemId);

        Problem problem = Problem.makeProblem(problemId, req, testCases, tags, userPrincipal.getId());
        problemRepository.insert(problem);
        tagRepository.saveAll(tags);

        testCaseRedisRepository.save(problemId, testCases);

        return new AddProblemResponse();
    }

    @Override
    public GetProblemResponse getProblem(Long id) {
        Problem problem = problemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 문제입니다."));

        return problemMapper.toDto(problem);
    }

    @Override
    public GetAllProblemResponse getAllProblem(GetAllProblemRequest req) {
        Page<Problem> problemPage = this.problemRepository.getAllProblem(req);
        int problemSize = problemPage.getNumberOfElements();

        GetAllProblemResponse response = new GetAllProblemResponse();
        if (problemSize > 0) {
            List<Problem> problems = problemPage.getContent();
            response.setProblems(problemMapper.toDtoList(problems));
            response.setPage(problemMapper.toPageDto(problemPage));
        }

        return response;
    }

    private Tag findOrMakeTag(String name) {
        return tagRepository.findByName(name)
                .orElse(Tag.makeTag(counterRepository.getNextSequence(MongoDB.TAG), name));
    }
}
