package yoonleeverse.onlinejudge.api.problem.dto;

import lombok.*;
import yoonleeverse.onlinejudge.api.common.dto.APIResponse;
import yoonleeverse.onlinejudge.api.problem.entity.ProgrammingLanguage;
import yoonleeverse.onlinejudge.api.problem.entity.Tag;
import yoonleeverse.onlinejudge.api.problem.entity.TestCaseExample;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper=false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetProblemResponse extends APIResponse {
    private Long id;
    private String title;

    private Integer timeLimit; // ms
    private Integer memoryLimit; // byte

    private String desc;
    private String inputDesc;
    private String outputDesc;
    private List<TestCaseExample> testCaseExamples;
    private List<ProgrammingLanguage> languages;
    private List<Tag> tags;
}
