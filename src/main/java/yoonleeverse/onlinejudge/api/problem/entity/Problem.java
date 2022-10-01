package yoonleeverse.onlinejudge.api.problem.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import yoonleeverse.onlinejudge.api.common.constant.MongoDB;
import yoonleeverse.onlinejudge.api.common.entity.BaseTimeEntity;
import yoonleeverse.onlinejudge.api.problem.dto.AddProblemRequest;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Document(collection = MongoDB.PROBLEM)
@Builder
public class Problem extends BaseTimeEntity {
    @Id @Setter private Long id;
    private String title;

    private int timeLimit; // ms
    private int memoryLimit; // byte

    private String desc;
    private String inputDesc;
    private String outputDesc;
    private List<TestCaseExample> testCaseExamples;
    private List<ProgrammingLanguage> languages;
    private List<TestCase> testCases;
    @DBRef private List<Tag> tags;

    private String userId;

    public static Problem makeProblem(long problemId, AddProblemRequest req, List<TestCase> testCases, List<Tag> tags, String userId) {
        Problem problem = Problem.builder()
                .id(problemId)
                .title(req.getTitle())
                .timeLimit(req.getTimeLimit())
                .memoryLimit(req.getMemoryLimit())
                .desc(req.getDesc())
                .inputDesc(req.getInputDesc())
                .outputDesc(req.getOutputDesc())
                .testCaseExamples(req.getTestCaseExamples())
                .languages(req.getLanguages())
                .testCases(testCases)
                .tags(tags)
                .userId(userId)
                .build();
        tags.forEach((tag) -> tag.addProblem(problem));

        return problem;
    }
}
