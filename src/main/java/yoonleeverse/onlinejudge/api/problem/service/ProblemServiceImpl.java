package yoonleeverse.onlinejudge.api.problem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import yoonleeverse.onlinejudge.api.common.constant.MongoDB;
import yoonleeverse.onlinejudge.api.common.dto.APIResponse;
import yoonleeverse.onlinejudge.api.common.repository.CounterRepository;
import yoonleeverse.onlinejudge.api.common.service.StorageService;
import yoonleeverse.onlinejudge.api.problem.dto.*;
import yoonleeverse.onlinejudge.api.problem.entity.*;
import yoonleeverse.onlinejudge.api.problem.mapper.ProblemMapper;
import yoonleeverse.onlinejudge.api.problem.repository.ProblemRepository;
import yoonleeverse.onlinejudge.api.problem.repository.TagRepository;
import yoonleeverse.onlinejudge.api.problem.repository.TestCaseRedisRepository;
import yoonleeverse.onlinejudge.security.UserPrincipal;

import java.util.ArrayList;
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
        checkIfExistingTitle(req);
        makeProblem(req, file, userPrincipal.getId());

        return new AddProblemResponse();
    }

    @Override
    public GetProblemResponse getProblem(Long id) {
        Problem problem = this.problemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 문제입니다."));

        return this.problemMapper.toDto(problem);
    }

    @Override
    public GetAllProblemResponse getAllProblem(GetAllProblemRequest req) {
        Page<Problem> problemPage = this.problemRepository.getAllProblem(req);

        GetAllProblemResponse response = new GetAllProblemResponse();
        response.setPage(this.problemMapper.toPageDto(problemPage));

        if (!problemPage.isEmpty()) {
            List<Problem> problems = problemPage.getContent();
            response.setProblems(this.problemMapper.toDtoList(problems));
        }

        return response;
    }

    @Override
    public APIResponse removeProblem(UserPrincipal userPrincipal, Long id) {
        Problem problem = this.problemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 문제입니다."));

        removeProblemFromTags(problem);

        this.testCaseRedisRepository.delete(problem.getId());
        this.problemRepository.delete(problem);

        return new APIResponse();
    }

    private void checkIfExistingTitle(AddProblemRequest req) {
        boolean isExistTitle = this.problemRepository.existsByTitle(req.getTitle());
        if (isExistTitle) {
            throw new RuntimeException("이미 존재하는 제목입니다.");
        }
    }

    private void makeProblem(AddProblemRequest req, MultipartFile file, String userId) {
        long problemId = this.counterRepository.getNextSequence(MongoDB.PROBLEM);

        Problem problem = this.problemMapper.toEntity(req);
        problem.setId(problemId);

        List<TestCase> testCases = this.storageService.loadTestCase(file, "problem/" + problemId);
        problem.setTestCases(testCases);

        List<Tag> tags = addProblemFromTags(problem, req.getTags());
        problem.setTags(tags);
        problem.setUserId(userId);

        this.testCaseRedisRepository.save(problemId, testCases);
        this.problemRepository.insert(problem);
    }

    private List<Tag> addProblemFromTags(Problem problem, List<String> tagNames) {
        List<Tag> tags = tagNames.stream()
                .map((tagName) -> {
                    Tag tag = findOrMakeTag(tagName);
                    tag.addProblem(problem);
                    return tag;
                })
                .collect(Collectors.toList());

        this.tagRepository.saveAll(tags);
        return tags;
    }

    private Tag findOrMakeTag(String name) {
        return this.tagRepository.findByName(name)
                .orElse(Tag.makeTag(this.counterRepository.getNextSequence(MongoDB.TAG), name));
    }

    private void removeProblemFromTags(Problem problem) {
        List<Long> tagIds = problem.getTags().stream()
                .map(Tag::getId)
                .collect(Collectors.toList());

        Iterable<Tag> tags = this.tagRepository.findAllById(tagIds);
        List<Tag> changedTags = new ArrayList<>();
        for (Tag tag : tags) {
            tag.removeProblem(problem);
            changedTags.add(tag);
        }
        this.tagRepository.saveAll(changedTags);
    }
}
