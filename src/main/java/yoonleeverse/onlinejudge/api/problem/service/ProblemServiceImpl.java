package yoonleeverse.onlinejudge.api.problem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import yoonleeverse.onlinejudge.api.common.service.StorageService;
import yoonleeverse.onlinejudge.api.problem.dto.AddProblemRequest;
import yoonleeverse.onlinejudge.api.problem.dto.AddProblemResponse;
import yoonleeverse.onlinejudge.api.problem.entity.ProblemEntity;
import yoonleeverse.onlinejudge.api.problem.entity.ProgrammingLanguage;
import yoonleeverse.onlinejudge.api.problem.repository.ProblemRepository;
import yoonleeverse.onlinejudge.security.UserPrincipal;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProblemServiceImpl implements ProblemService {

    private final ProblemRepository problemRepository;
    private final StorageService storageService;

    @Override
    public AddProblemResponse addProblem(UserPrincipal userPrincipal, AddProblemRequest req) {
        AddProblemResponse addProblemResponse = new AddProblemResponse();

        try {
            boolean isExistTitle = problemRepository.existsByTitle(req.getTitle());
            if (isExistTitle) {
                throw new RuntimeException("이미 존재하는 제목입니다.");
            }

            ProblemEntity problem = ProblemEntity.builder()
                    .title(req.getTitle())
                    .timeLimit(req.getTimeLimit())
                    .memoryLimit(req.getMemoryLimit())
                    .desc(req.getDesc())
                    .inputDesc(req.getInputDesc())
                    .outputDesc(req.getOutputDesc())
                    .testCaseExamples(req.getTestCaseExamples())
                    .languages(req.getLanguages().stream()
                            .map(e -> ProgrammingLanguage.valueOf(e.toUpperCase())).collect(Collectors.toList()))
                    .build();
            problemRepository.saveWithId(problem);

            storageService.store(req.getFile(), "problem/" + problem.getId());
        } catch (Exception e) {
            addProblemResponse.setSuccess(false);
            addProblemResponse.setErrMsg(e.getMessage());
        }

        return addProblemResponse;
    }
}
