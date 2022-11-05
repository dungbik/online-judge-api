package yoonleeverse.onlinejudge.api.problem.entity;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import yoonleeverse.onlinejudge.api.common.constant.MongoDB;
import yoonleeverse.onlinejudge.api.common.entity.BaseTimeEntity;

import java.util.List;

@Getter
@Document(collection = MongoDB.PROBLEM)
@Builder
@EqualsAndHashCode(callSuper=false, of="id")
public class Problem extends BaseTimeEntity {
    @Id @Setter private Long id;
    private String title;

    private int timeLimit; // ms
    private int memoryLimit; // byte

    private String desc;
    private String inputDesc;
    private String outputDesc;
    private List<TestCase> testCaseExamples;
    private List<ProgrammingLanguage> languages;
    private List<TestCase> testCases;
    @DBRef private List<Tag> tags;

    private SubmissionHistory submissionHistory;

    private String userId;

    public void setTestCaseExamples(List<TestCase> testCaseExamples) {
        this.testCaseExamples = testCaseExamples;
    }

    public void setTestCases(List<TestCase> testCases) {
        this.testCases = testCases;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
