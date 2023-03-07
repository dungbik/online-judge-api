package yoonleeverse.onlinejudge.api.problem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import yoonleeverse.onlinejudge.api.common.constant.MongoDB;
import yoonleeverse.onlinejudge.api.common.dto.APIResponse;
import yoonleeverse.onlinejudge.api.common.repository.CounterRepository;
import yoonleeverse.onlinejudge.api.common.service.StorageService;
import yoonleeverse.onlinejudge.api.problem.dto.GetAllTagResponse;
import yoonleeverse.onlinejudge.api.problem.dto.*;
import yoonleeverse.onlinejudge.api.problem.entity.*;
import yoonleeverse.onlinejudge.api.problem.mapper.ProblemMapper;
import yoonleeverse.onlinejudge.api.problem.repository.ProblemRepository;
import yoonleeverse.onlinejudge.api.problem.repository.TagRepository;
import yoonleeverse.onlinejudge.api.problem.repository.TestCaseRedisRepository;
import yoonleeverse.onlinejudge.security.UserPrincipal;
import yoonleeverse.onlinejudge.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
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
        this.addProblem(null, req, file, userPrincipal.getEmail());

        return new AddProblemResponse();
    }

    @Override
    public GetProblemResponse getProblem(Long id) {
        Problem problem = this.findProblem(id);

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
        Problem problem = this.findProblem(id);

        this.removeProblemFromTags(problem);

        this.testCaseRedisRepository.delete(problem.getId());
        this.problemRepository.delete(problem);

        return new APIResponse();
    }

    @Override
    public APIResponse updateProblem(UserPrincipal userPrincipal, Long id, AddProblemRequest req, MultipartFile file) {
        Problem problem = this.findProblem(id);
        this.addProblem(problem, req, file, userPrincipal.getEmail());

        return new APIResponse();
    }

    @Override
    public GetAllTagResponse getAllTag() {
        List<TagVO> tags = tagRepository.findAll().stream()
                .map(e -> TagVO.of(e.getId(), e.getName()))
                .collect(Collectors.toList());

        return new GetAllTagResponse(tags);
    }

    private Problem findProblem(Long id) {
        Problem problem = this.problemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 문제입니다."));
        return problem;
    }

    /**
     * 추가/수정하려는 문제 정보를 검증 후, 실제로 등록/갱신을 수행한다.
     * @param problem null 일 경우 새로운 문제 생성, 아닐 경우 문제 수정
     * @param req 문제를 추가하기 위한 정보
     * @param file 테스트케이스 압축파일
     * @param email 문제를 추가한 유저의 이메일
     */
    private void addProblem(Problem problem, AddProblemRequest req, MultipartFile file, String email) {
        boolean isNew = problem == null;
        if (isNew || !problem.getTitle().equals(req.getTitle()))
            this.checkIfExistingTitle(req);

        long problemId = isNew ? this.counterRepository.getNextSequence(MongoDB.PROBLEM) : problem.getId();

        problem = this.problemMapper.toEntity(req);
        problem.setId(problemId);

        AtomicInteger index = new AtomicInteger();
        List<TestCase> exampleList = req.getTestCaseExamples().stream()
                .map((e) -> new TestCase(index.getAndIncrement(), e.getInput(), e.getOutput(), StringUtil.encryptMD5(e.getOutput())))
                .collect(Collectors.toList());
        problem.setTestCaseExamples(exampleList);

        if (file != null) {
            List<TestCase> testCases = this.storageService.loadTestCase(file, "problem/" + problemId);
            problem.setTestCases(testCases);
            if (!isNew) {
                this.testCaseRedisRepository.delete(problemId);
            }
            this.testCaseRedisRepository.save(problemId, testCases);
        }

        List<Tag> tags = this.addProblemFromTags(problem, req.getTags());
        problem.setTags(tags);
        problem.setUserId(email);

        if (isNew) {
            this.problemRepository.insert(problem);
        } else {
            this.removeProblemFromTags(problem);
            this.problemRepository.save(problem);
        }
    }

    private void checkIfExistingTitle(AddProblemRequest req) {
        boolean isExistTitle = this.problemRepository.existsByTitle(req.getTitle());
        if (isExistTitle) {
            throw new RuntimeException("이미 존재하는 제목입니다.");
        }
    }

    private List<Tag> addProblemFromTags(Problem problem, List<String> tagNames) {
        List<Tag> tags = tagNames.stream()
                .map((tagName) -> {
                    Tag tag = findOrMakeTag(tagName);
                    tag.addProblem(problem);
                    return tag;
                })
                .collect(Collectors.toList());

        return this.tagRepository.saveAll(tags);
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
