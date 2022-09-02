package yoonleeverse.onlinejudge.api.problem.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import yoonleeverse.onlinejudge.api.common.entity.BaseTimeEntity;

import java.util.List;

@Getter
@Document(collection = "problems")
@Builder
public class ProblemEntity extends BaseTimeEntity {
    @Id @Setter private Long id;
    private String title;

    private int timeLimit; // ms
    private int memoryLimit; // byte

    private String desc;
    private String inputDesc;
    private String outputDesc;
    private List<TestCaseExample> testCaseExamples;

    private List<ProgrammingLanguage> languages;
}
